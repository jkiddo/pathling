/*
 * Copyright 2022 Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.csiro.pathling.test.integration;

import static au.csiro.pathling.test.assertions.Assertions.assertMatches;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.ALL_EQUIVALENCES;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.AUTOMAP_INPUT_URI;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_AST_VIC;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_107963000;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_284551006;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_720471000168102;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_72940011000036107;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_VER_107963000;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_VER_284551006;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_VER_403190006;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CD_SNOMED_VER_63816008;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CM_AUTOMAP_DEFAULT;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.CM_HIST_ASSOCIATIONS;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.INEXACT;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.SNOMED_URI;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.setOfSimpleFrom;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.simpleOf;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.snomedCoding;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.snomedSimple;
import static au.csiro.pathling.test.helpers.TerminologyHelpers.testSimple;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.proxyAllTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import au.csiro.pathling.fhirpath.encoding.SimpleCoding;
import au.csiro.pathling.terminology.ConceptTranslator;
import au.csiro.pathling.terminology.Relation;
import au.csiro.pathling.terminology.TerminologyService;
import au.csiro.pathling.terminology.UUIDFactory;
import au.csiro.pathling.test.fixtures.ConceptTranslatorBuilder;
import au.csiro.pathling.test.fixtures.RelationBuilder;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.github.tomakehurst.wiremock.recording.RecordSpecBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Coding;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Piotr Szul
 */

// TODO: terminology cache: Implement if needed for the new cachable service
@Tag("Tranche2")
@Disabled
@Slf4j
class TerminologyServiceIntegrationTest extends WireMockTest {

  @Autowired
  FhirContext fhirContext;

  @Autowired
  TerminologyService terminologyService;

  @MockBean
  UUIDFactory uuidFactory;

  @Value("${pathling.test.recording.terminologyServerUrl}")
  String recordingTxServerUrl;

  @BeforeEach
  @Override
  void setUp() {
    super.setUp();
    if (isRecordMode()) {
      wireMockServer.resetAll();
      log.warn("Proxying all request to: {}", recordingTxServerUrl);
      stubFor(proxyAllTo(recordingTxServerUrl));
    }
  }

  @AfterEach
  @Override
  void tearDown() {
    if (isRecordMode()) {
      log.warn("Recording snapshots to: {}", wireMockServer.getOptions().filesRoot());
      wireMockServer
          .snapshotRecord(new RecordSpecBuilder().matchRequestBodyWithEqualToJson(true, false));
    }
    super.tearDown();
  }

  @Test
  void testCorrectlyTranslatesKnownAndUnknownCodes() {

    final ConceptTranslator actualTranslation = terminologyService.translate(
        Arrays.asList(simpleOf(CD_SNOMED_72940011000036107), snomedSimple("444814009")),
        CM_HIST_ASSOCIATIONS, false, ALL_EQUIVALENCES, null);

    final ConceptTranslator expectedTranslation = ConceptTranslatorBuilder.empty()
        .put(CD_SNOMED_72940011000036107, CD_SNOMED_720471000168102)
        .build();
    assertEquals(expectedTranslation, actualTranslation);
  }

  @Test
  void testCorrectlyTranslatesInReverse() {

    final ConceptTranslator actualTranslation = terminologyService.translate(
        Arrays.asList(simpleOf(CD_SNOMED_720471000168102), snomedSimple("444814009")),
        CM_HIST_ASSOCIATIONS, true, ALL_EQUIVALENCES, null);

    final ConceptTranslator expectedTranslation = ConceptTranslatorBuilder.empty()
        .put(CD_SNOMED_720471000168102, CD_SNOMED_72940011000036107)
        .build();
    assertEquals(expectedTranslation, actualTranslation);
  }

  @Test
  void testAutomap() {
    final Coding input = new Coding(AUTOMAP_INPUT_URI, "shortness of breath", null);
    final String version = "http://snomed.info/sct/32506021000036107/version/20221031";

    final Coding result1 = snomedCoding("267036007", "Dyspnea (finding)", version);
    final Coding result2 = snomedCoding("390870001",
        "Short of breath dressing/undressing (finding)", version);
    final Coding result3 = snomedCoding("1217110005",
        "Dyspnea when bending forward (finding)", version);
    final Coding result4 = snomedCoding("161941007", "Dyspnea at rest (finding)", version);
    final Coding result5 = snomedCoding("60845006", "Dyspnea on exertion (finding)", version);

    final String target = "http://snomed.info/sct?fhir_vs=ecl/(%3C%3C%2064572001%20%7CDisease%7C%20OR%20%3C%3C%20404684003%20%7CClinical%20finding%7C)";

    final ConceptTranslator actualTranslation = terminologyService.translate(
        List.of(simpleOf(input)), CM_AUTOMAP_DEFAULT, false, INEXACT, target);

    final ConceptTranslator expectedTranslation = ConceptTranslatorBuilder.empty()
        .put(input, result1, result2, result3, result4, result5)
        .build();
    assertEquals(expectedTranslation, actualTranslation);
  }

  @Test
  void testAutomapDescriptionId() {
    final Coding input = new Coding(AUTOMAP_INPUT_URI, "397889019", null);
    final String version = "http://snomed.info/sct/32506021000036107/version/20221031";

    final Coding result = snomedCoding("267036007", "Dyspnea (finding)", version);

    final String target = "http://snomed.info/sct?fhir_vs";

    final ConceptTranslator actualTranslation = terminologyService.translate(
        List.of(simpleOf(input)), CM_AUTOMAP_DEFAULT, false, INEXACT, target);

    final ConceptTranslator expectedTranslation = ConceptTranslatorBuilder.empty()
        .put(input, result)
        .build();
    assertEquals(expectedTranslation, actualTranslation);
  }

  // TODO: Enable when fixed in terminology server, that is it does not accept ignore systems in
  //  codings.
  @Test
  @Disabled
  void testIgnoresUnknownSystems() {

    final ConceptTranslator actualTranslation = terminologyService.translate(
        Arrays.asList(testSimple("72940011000036107"), testSimple("444814009")),
        CM_HIST_ASSOCIATIONS, false, ALL_EQUIVALENCES, null);

    final ConceptTranslator expectedTranslation = ConceptTranslatorBuilder.empty().build();
    assertEquals(expectedTranslation, actualTranslation);
  }

  @Test
  void testFailsForUnknownConceptMap() {

    final ResourceNotFoundException error = assertThrows(ResourceNotFoundException.class,
        () -> terminologyService.translate(
            Arrays.asList(simpleOf(CD_SNOMED_72940011000036107), snomedSimple("444814009")),
            "http://snomed.info/sct?fhir_cm=xxxx", false,
            ALL_EQUIVALENCES, null));

    assertMatches(
        "Error in response entry : HTTP 404 : "
            + "\\[.+\\]: "
            + "Unable to find ConceptMap with URI http://snomed\\.info/sct\\?fhir_cm=xxxx",
        error.getMessage());
  }

  @Test
  void testCorrectlyIntersectKnownAndUnknownSystems() {
    final Set<SimpleCoding> expansion = terminologyService
        .intersect("http://snomed.info/sct?fhir_vs=refset/32570521000036109",
            setOfSimpleFrom(CD_SNOMED_284551006, CD_SNOMED_VER_403190006,
                CD_SNOMED_72940011000036107, CD_AST_VIC,
                new Coding("uuid:unknown", "unknown", "Unknown")
            ));

    // TODO: Ask John - why the expansion is versioned if we include the CD_AST_VIC, but unversioned
    //  otherwise? As this will affect the functioning of memberOf (since it uses SimpleCoding
    //  equality). Also if two versioned SNOMED codings are requested the response contains their
    //  unversioned versions.
    assertEquals(setOfSimpleFrom(CD_SNOMED_VER_284551006, CD_SNOMED_VER_403190006), expansion);
  }


  @Test
  void testCorrectlyBuildsClosureKnownAndUnknownSystems() {
    when(uuidFactory.nextUUID())
        .thenReturn(UUID.fromString("5d1b976d-c50c-445a-8030-64074b83f355"));

    final Relation actualRelation = terminologyService
        .getSubsumesRelation(
            setOfSimpleFrom(CD_SNOMED_107963000, CD_SNOMED_VER_63816008,
                CD_SNOMED_72940011000036107, CD_AST_VIC,
                new Coding("uuid:unknown", "unknown", "Unknown")
            ));

    // It appears that in the response all codings are versioned regardless
    // of whether the version was present in the request
    final Relation expectedRelation = RelationBuilder.empty()
        .add(CD_SNOMED_VER_107963000, CD_SNOMED_VER_63816008).build();
    assertEquals(expectedRelation, actualRelation);
  }

  @Test
  void testUserAgentHeader() {
    final Collection<SimpleCoding> codings = new HashSet<>(
        List.of(new SimpleCoding(SNOMED_URI, "48429009")));
    terminologyService.intersect(SNOMED_URI + "?fhir_vs", codings);

    verify(anyRequestedFor(urlPathMatching("/fhir/(.*)"))
        .withHeader("User-Agent", matching("pathling/(.*)")));
  }

}
