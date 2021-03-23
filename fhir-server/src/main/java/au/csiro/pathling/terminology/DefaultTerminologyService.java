/*
 * Copyright © 2018-2021, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.terminology;

import static au.csiro.pathling.terminology.ClosureMapping.relationFromConceptMap;
import static au.csiro.pathling.utilities.Preconditions.checkNotNull;

import au.csiro.pathling.fhir.TerminologyClient;
import au.csiro.pathling.fhirpath.encoding.SimpleCoding;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.UriParam;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;

/**
 * Default implementation of TerminologyService using a backend terminology server.
 */
@Slf4j
public class DefaultTerminologyService implements TerminologyService {


  @Nonnull
  private final FhirContext fhirContext;

  @Nonnull
  private final TerminologyClient terminologyClient;

  /**
   * @param fhirContext The {@link FhirContext} used to interpret responses
   * @param terminologyClient The {@link TerminologyClient} used to issue requests
   */
  public DefaultTerminologyService(@Nonnull final FhirContext fhirContext,
      @Nonnull final TerminologyClient terminologyClient) {
    this.fhirContext = fhirContext;
    this.terminologyClient = terminologyClient;
  }

  @Nonnull
  private Stream<SimpleCoding> filterByKnownSystems(
      @Nonnull final Collection<SimpleCoding> codings) {

    // filter out codings with code systems unknown to the terminology server
    final Set<String> allCodeSystems = codings.stream()
        .filter(SimpleCoding::isDefined)
        .map(SimpleCoding::getSystem)
        .collect(Collectors.toSet());

    final Set<String> knownCodeSystems = allCodeSystems.stream().filter(codeSystem -> {
      final UriParam uri = new UriParam(codeSystem);
      final List<CodeSystem> knownSystems = terminologyClient.searchCodeSystems(
          uri, new HashSet<>(Collections.singletonList("id")));
      return !(knownSystems == null || knownSystems.isEmpty());
    }).collect(Collectors.toSet());

    if (!knownCodeSystems.equals(allCodeSystems)) {
      final Collection<String> unrecognizedCodeSystems = new HashSet<>(allCodeSystems);
      unrecognizedCodeSystems.removeAll(knownCodeSystems);
      log.warn("Terminology server does not recognize these coding systems: {}",
          unrecognizedCodeSystems);
    }

    return codings.stream()
        .filter(coding -> knownCodeSystems.contains(coding.getSystem()));
  }

  @Nonnull
  @Override
  public ConceptTranslator translate(@Nonnull final Collection<SimpleCoding> codings,
      @Nonnull final String conceptMapUrl, final boolean reverse,
      @Nonnull final Collection<ConceptMapEquivalence> equivalences) {

    final List<SimpleCoding> uniqueCodings = codings.stream().distinct()
        .collect(Collectors.toUnmodifiableList());
    // create bundle
    final Bundle translateBatch = TranslateMapping
        .toRequestBundle(uniqueCodings, conceptMapUrl, reverse);
    final Bundle result = terminologyClient.batch(translateBatch);
    return TranslateMapping
        .fromResponseBundle(checkNotNull(result), uniqueCodings, equivalences, fhirContext);
  }

  @Nonnull
  @Override
  public Relation getSubsumesRelation(@Nonnull Collection<SimpleCoding> systemAndCodes) {
    final List<Coding> codings = filterByKnownSystems(systemAndCodes)
        .distinct()
        .map(SimpleCoding::toCoding)
        .collect(Collectors.toUnmodifiableList());
    // recreate the systemAndCodes dataset from the list not to execute the query again.
    // Create a unique name for the closure table for this code system, based upon the
    // expressions of the input, argument and the CodeSystem URI.
    final String closureName = UUID.randomUUID().toString();
    log.info("Sending $closure request to terminology service with name '{}' and {} codings",
        closureName, codings.size());
    terminologyClient.initialiseClosure(new StringType(closureName));
    final ConceptMap closureResponse =
        terminologyClient.closure(new StringType(closureName), codings);
    checkNotNull(closureResponse);
    return relationFromConceptMap(closureResponse);
  }
}
