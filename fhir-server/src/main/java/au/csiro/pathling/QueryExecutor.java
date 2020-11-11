/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling;

import static au.csiro.pathling.QueryHelpers.joinOnId;
import static au.csiro.pathling.utilities.Preconditions.checkArgument;
import static au.csiro.pathling.utilities.Preconditions.checkPresent;

import au.csiro.pathling.QueryHelpers.JoinType;
import au.csiro.pathling.fhir.TerminologyClient;
import au.csiro.pathling.fhir.TerminologyClientFactory;
import au.csiro.pathling.fhirpath.FhirPath;
import au.csiro.pathling.fhirpath.parser.ParserContext;
import au.csiro.pathling.io.ResourceReader;
import ca.uhn.fhir.context.FhirContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import lombok.Getter;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Contains functionality common to query executors.
 *
 * @author John Grimes
 */
@Getter
public abstract class QueryExecutor {

  @Nonnull
  private final Configuration configuration;

  @Nonnull
  private final FhirContext fhirContext;

  @Nonnull
  private final SparkSession sparkSession;

  @Nonnull
  private final ResourceReader resourceReader;

  @Nonnull
  private final Optional<TerminologyClient> terminologyClient;

  @Nonnull
  private final Optional<TerminologyClientFactory> terminologyClientFactory;

  protected QueryExecutor(@Nonnull final Configuration configuration,
      @Nonnull final FhirContext fhirContext, @Nonnull final SparkSession sparkSession,
      @Nonnull final ResourceReader resourceReader,
      @Nonnull final Optional<TerminologyClient> terminologyClient,
      @Nonnull final Optional<TerminologyClientFactory> terminologyClientFactory) {
    this.configuration = configuration;
    this.fhirContext = fhirContext;
    this.sparkSession = sparkSession;
    this.resourceReader = resourceReader;
    this.terminologyClient = terminologyClient;
    this.terminologyClientFactory = terminologyClientFactory;
  }

  /**
   * Joins the datasets in a list together, using their resource identity columns.
   * <p>
   * Note that this should only be used in contexts where the input context is a single resource -
   * it does not support joining using grouping columns.
   */
  @Nonnull
  protected static Dataset<Row> joinExpressions(@Nonnull final List<FhirPath> expressions) {
    checkArgument(!expressions.isEmpty(), "expressions must not be empty");

    FhirPath previous = expressions.get(0);
    Dataset<Row> result = previous.getDataset();

    for (int i = 1; i < expressions.size(); i++) {
      final FhirPath current = expressions.get(i);
      final Column idColumn = checkPresent(previous.getIdColumn());
      result = joinOnId(result, idColumn, current, JoinType.LEFT_OUTER);
      previous = current;
    }

    return result;
  }

  @Nonnull
  protected static Dataset<Row> applyFilters(@Nonnull final Dataset<Row> dataset,
      @Nonnull final Collection<FhirPath> filters) {
    // Get the value column from each filter expression, and combine them with AND logic.
    final Optional<Column> filterCondition = filters.stream()
        .flatMap(filter -> filter.getValueColumns().stream())
        .reduce(Column::and);

    // Return a Dataset filtered using the combined filter conditions.
    return filterCondition.isPresent()
           ? dataset.filter(filterCondition.get())
           : dataset;
  }

  protected ParserContext buildParserContext(@Nonnull final FhirPath inputContext) {
    return buildParserContext(inputContext, Collections.emptyList());
  }

  protected ParserContext buildParserContext(@Nonnull final FhirPath inputContext,
      @Nonnull final List<Column> groupingColumns) {
    return new ParserContext(inputContext, fhirContext, sparkSession, resourceReader,
        terminologyClient, terminologyClientFactory, groupingColumns);
  }

}
