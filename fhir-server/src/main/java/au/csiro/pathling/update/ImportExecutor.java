/*
 * Copyright © 2018-2022, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.update;

import static au.csiro.pathling.utilities.Preconditions.checkUserInput;

import au.csiro.pathling.caching.CacheInvalidator;
import au.csiro.pathling.encoders.FhirEncoders;
import au.csiro.pathling.encoders.UnsupportedResourceError;
import au.csiro.pathling.errors.InvalidUserInputError;
import au.csiro.pathling.errors.SecurityError;
import au.csiro.pathling.fhir.FhirContextFactory;
import au.csiro.pathling.io.AccessRules;
import au.csiro.pathling.io.Database;
import au.csiro.pathling.io.PersistenceScheme;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations.ResourceType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.IssueType;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.UrlType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Encapsulates the execution of an import operation.
 *
 * @author John Grimes
 * @see <a href="https://pathling.csiro.au/docs/import.html">Import</a>
 */
@Component
@Profile({"core", "import"})
@Slf4j
public class ImportExecutor {

  @Nonnull
  private final SparkSession spark;

  @Nonnull
  private final Database database;

  @Nonnull
  private final FhirEncoders fhirEncoders;

  @Nonnull
  private final FhirContextFactory fhirContextFactory;

  @Nonnull
  private final Optional<CacheInvalidator> cacheInvalidator;

  @Nonnull
  private final Optional<AccessRules> accessRules;

  /**
   * @param spark a {@link SparkSession} for resolving Spark queries
   * @param database a {@link Database} for writing resources
   * @param fhirEncoders a {@link FhirEncoders} object for converting data back into HAPI FHIR
   * @param fhirContextFactory a {@link FhirContextFactory} for constructing FhirContext objects in
   * the context of parallel processing
   * @param cacheInvalidator a {@link CacheInvalidator} for invalidating caches upon import
   * @param accessRules a {@link AccessRules} for validating access to URLs
   */
  public ImportExecutor(@Nonnull final SparkSession spark,
      @Nonnull final Database database,
      @Nonnull final FhirEncoders fhirEncoders,
      @Nonnull final FhirContextFactory fhirContextFactory,
      @Nonnull final Optional<CacheInvalidator> cacheInvalidator,
      @Nonnull final Optional<AccessRules> accessRules) {
    this.spark = spark;
    this.database = database;
    this.fhirEncoders = fhirEncoders;
    this.fhirContextFactory = fhirContextFactory;
    this.cacheInvalidator = cacheInvalidator;
    this.accessRules = accessRules;
  }

  /**
   * Executes an import request.
   *
   * @param inParams a FHIR {@link Parameters} object describing the import request
   * @return a FHIR {@link OperationOutcome} resource describing the result
   */
  @Nonnull
  public OperationOutcome execute(@Nonnull @ResourceParam final Parameters inParams) {
    // Parse and validate the JSON request.
    final List<ParametersParameterComponent> sourceParams = inParams.getParameter().stream()
        .filter(param -> "source".equals(param.getName())).collect(Collectors.toList());
    if (sourceParams.isEmpty()) {
      throw new InvalidUserInputError("Must provide at least one source parameter");
    }
    log.info("Received $import request");

    // For each input within the request, read the resources of the declared type and create
    // the corresponding table in the warehouse.
    for (final ParametersParameterComponent sourceParam : sourceParams) {
      final ParametersParameterComponent resourceTypeParam = sourceParam.getPart().stream()
          .filter(param -> "resourceType".equals(param.getName()))
          .findFirst()
          .orElseThrow(
              () -> new InvalidUserInputError("Must provide resourceType for each source"));
      final ParametersParameterComponent urlParam = sourceParam.getPart().stream()
          .filter(param -> "url".equals(param.getName()))
          .findFirst()
          .orElseThrow(
              () -> new InvalidUserInputError("Must provide url for each source"));
      // The mode parameter defaults to 'overwrite'.
      final ImportMode importMode = sourceParam.getPart().stream()
          .filter(param -> "mode".equals(param.getName()) &&
              param.getValue() instanceof CodeType)
          .findFirst()
          .map(param -> ImportMode.fromCode(((CodeType) param.getValue()).asStringValue()))
          .orElse(ImportMode.OVERWRITE);
      final String resourceCode = ((CodeType) resourceTypeParam.getValue()).getCode();
      final ResourceType resourceType = ResourceType.fromCode(resourceCode);

      // Get an encoder based on the declared resource type within the source parameter.
      final ExpressionEncoder<IBaseResource> fhirEncoder;
      try {
        fhirEncoder = fhirEncoders.of(resourceType.toCode());
      } catch (final UnsupportedResourceError e) {
        throw new InvalidUserInputError("Unsupported resource type: " + resourceCode);
      }

      // Check that the user is authorized to execute the operation.
      final Dataset<String> jsonStrings = checkAuthorization(urlParam);

      // Parse each line into a HAPI FHIR object, then encode to a Spark dataset.
      final Dataset<IBaseResource> resources = jsonStrings.map(jsonToResourceConverter(),
          fhirEncoder);

      log.info("Importing {} resources ({})", resourceType.toCode(), importMode.getCode());
      if (importMode == ImportMode.OVERWRITE) {
        database.overwrite(resourceType, resources.toDF());
      } else {
        database.merge(resourceType, resources.toDF());
      }
    }

    // We return 200, as this operation is currently synchronous.
    log.info("Import complete");

    // Invalidate all caches following the import.
    cacheInvalidator.ifPresent(CacheInvalidator::invalidateAll);

    // Construct a response.
    final OperationOutcome opOutcome = new OperationOutcome();
    final OperationOutcomeIssueComponent issue = new OperationOutcomeIssueComponent();
    issue.setSeverity(IssueSeverity.INFORMATION);
    issue.setCode(IssueType.INFORMATIONAL);
    issue.setDiagnostics("Data import completed successfully");
    opOutcome.getIssue().add(issue);
    return opOutcome;
  }

  @Nonnull
  private Dataset<String> checkAuthorization(@Nonnull final ParametersParameterComponent urlParam) {
    final String url = ((UrlType) urlParam.getValue()).getValueAsString();
    final String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
    final String convertedUrl = PersistenceScheme.convertS3ToS3aUrl(decodedUrl);
    final Dataset<String> jsonStrings;
    try {
      accessRules.ifPresent(ar -> ar.checkCanImportFrom(convertedUrl));
      final FilterFunction<String> nonBlanks = s -> !s.isBlank();
      jsonStrings = spark.read().textFile(convertedUrl).filter(nonBlanks);
    } catch (final SecurityError e) {
      throw new InvalidUserInputError("Not allowed to import from URL: " + convertedUrl, e);
    } catch (final Exception e) {
      throw new InvalidUserInputError("Error reading from URL: " + convertedUrl, e);
    }
    return jsonStrings;
  }

  @Nonnull
  private MapFunction<String, IBaseResource> jsonToResourceConverter() {
    final FhirContextFactory localFhirContextFactory = this.fhirContextFactory;
    return (json) -> {
      final IBaseResource resource = localFhirContextFactory.build().newJsonParser()
          .parseResource(json);
      // All imported resources must have an ID set.
      checkUserInput(!resource.getIdElement().isEmpty(), "Encountered a resource with no ID");
      return resource;
    };
  }

  public enum ImportMode {
    /**
     * Results in all existing resources of the specified type to be deleted and replaced with the
     * contents of the source file.
     */
    OVERWRITE("overwrite"),

    /**
     * Matches existing resources with updated resources in the source file based on their ID, and
     * either update the existing resources or add new resources as appropriate.
     */
    MERGE("merge");

    @Nonnull
    @Getter
    private final String code;

    ImportMode(@Nonnull final String code) {
      this.code = code;
    }

    @Nullable
    public static ImportMode fromCode(@Nonnull final String code) {
      for (final ImportMode mode : values()) {
        if (mode.code.equals(code)) {
          return mode;
        }
      }
      return null;
    }

  }

}
