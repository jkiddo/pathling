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

package au.csiro.pathling.fhir;

import static au.csiro.pathling.utilities.Preconditions.checkNotNull;

import au.csiro.pathling.config.TerminologyAuthConfiguration;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.http.client.HttpClient;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The client interface to FHIR terminology operations.
 */
public interface TerminologyClient {

  Logger log = LoggerFactory.getLogger(TerminologyClient.class);

  /**
   * @param url the URL of the value set to validate against
   * @param system the system of the code to validate
   * @param version the version of the code system to validate against
   * @param code the code to validate
   * @return a {@link Parameters} resource
   * @see <a
   * href="https://www.hl7.org/fhir/R4/valueset-operation-validate-code.html">ValueSet/$validate-code</a>
   */
  @Operation(name = "$validate-code", type = ValueSet.class, idempotent = true)
  @Nonnull
  Parameters validateCode(
      @Nonnull @OperationParam(name = "url") UriType url,
      @Nonnull @OperationParam(name = "system") UriType system,
      @Nullable @OperationParam(name = "systemVersion") StringType version,
      @Nonnull @OperationParam(name = "code") CodeType code
  );

  /**
   * Builds a validate code operation that can be customized and executed later.
   *
   * @param url the URL of the value set to validate against
   * @param system the system of the code to validate
   * @param version the version of the code system to validate against
   * @param code the code to validate
   * @return an {@link IOperationUntypedWithInput} that can be customized and executed later
   * @see <a
   * href="https://www.hl7.org/fhir/R4/valueset-operation-validate-code.html">ValueSet/$validate-code</a>
   */
  IOperationUntypedWithInput<Parameters> buildValidateCode(@Nonnull UriType url,
      @Nonnull UriType system,
      @Nullable StringType version, @Nonnull CodeType code);

  /**
   * @param url the URL of the concept map to use for translation
   * @param system the system of the code to translate
   * @param version the version of the code system to translate from
   * @param code the code to translate
   * @param reverse if true, the translation will be reversed
   * @param target the URL of the value set within which the translation is sought
   * @return a {@link Parameters} resource
   * @see <a
   * href="https://www.hl7.org/fhir/R4/operation-conceptmap-translate.html">ConceptMap/$translate</a>
   */
  @Operation(name = "$translate", type = CodeSystem.class, idempotent = true)
  @Nonnull
  Parameters translate(
      @Nonnull @OperationParam(name = "url") UriType url,
      @Nonnull @OperationParam(name = "system") UriType system,
      @Nullable @OperationParam(name = "version") StringType version,
      @Nonnull @OperationParam(name = "code") CodeType code,
      @Nullable @OperationParam(name = "reverse") BooleanType reverse,
      @Nullable @OperationParam(name = "target") UriType target
  );

  /**
   * Builds a translate operation that can be customized and executed later.
   *
   * @param url the URL of the concept map to use for translation
   * @param system the system of the code to translate
   * @param version the version of the code system to translate from
   * @param code the code to translate
   * @param reverse if true, the translation will be reversed
   * @param target the URL of the value set within which the translation is sought
   * @return an {@link IOperationUntypedWithInput} that can be customized and executed later
   * @see <a
   * href="https://www.hl7.org/fhir/R4/operation-conceptmap-translate.html">ConceptMap/$translate</a>
   */
  @Nonnull
  IOperationUntypedWithInput<Parameters> buildTranslate(@Nonnull UriType url,
      @Nonnull UriType system,
      @Nullable StringType version, @Nonnull CodeType code, @Nullable BooleanType reverse,
      @Nullable UriType target);

  /**
   * @param codeA the code that will be tested to check if it subsumes codeB
   * @param codeB the code that will be tested to check if it is subsumed by codeA
   * @param system the system of the codes being tested
   * @param version the version of the code system that the codes are from
   * @return a {@link Parameters} resource
   * @see <a
   * href="https://www.hl7.org/fhir/R4/codesystem-operation-subsumes.html">CodeSystem/$subsumes</a>
   */
  @Operation(name = "$subsumes", type = CodeSystem.class, idempotent = true)
  @Nonnull
  Parameters subsumes(
      @Nonnull @OperationParam(name = "codeA") CodeType codeA,
      @Nonnull @OperationParam(name = "codeB") CodeType codeB,
      @Nonnull @OperationParam(name = "system") UriType system,
      @Nullable @OperationParam(name = "version") StringType version
  );

  /**
   * Builds a subsumes operation that can be customized and executed later.
   *
   * @param codeA the code that will be tested to check if it subsumes codeB
   * @param codeB the code that will be tested to check if it is subsumed by codeA
   * @param system the system of the codes being tested
   * @param version the version of the code system that the codes are from
   * @return an {@link IOperationUntypedWithInput} that can be customized and executed later
   * @see <a
   * href="https://www.hl7.org/fhir/R4/codesystem-operation-subsumes.html">CodeSystem/$subsumes</a>
   */
  @Nonnull
  IOperationUntypedWithInput<Parameters> buildSubsumes(@Nonnull CodeType codeA,
      @Nonnull CodeType codeB, @Nonnull UriType system, @Nullable StringType version);

  /**
   * @param system the system of the code
   * @param version the version of the code system
   * @param code the code to lookup
   * @param property the property or properties to be returned in the response
   * @return a {@link Parameters} resource
   * @see <a
   * href="https://www.hl7.org/fhir/R4/codesystem-operation-lookup.html">CodeSystem/$lookup</a>
   */
  @Operation(name = "$lookup", type = CodeSystem.class, idempotent = true)
  @Nonnull
  Parameters lookup(
      @Nonnull @OperationParam(name = "system") UriType system,
      @Nullable @OperationParam(name = "version") StringType version,
      @Nonnull @OperationParam(name = "code") CodeType code,
      @Nullable @OperationParam(name = "property") CodeType property
  );

  /**
   * Builds a lookup operation that can be customized and executed later.
   *
   * @param system the system of the code
   * @param version the version of the code system
   * @param code the code to lookup
   * @param property the property or properties to be returned in the response
   * @return an {@link IOperationUntypedWithInput} that can be customized and executed later
   * @see <a
   * href="https://www.hl7.org/fhir/R4/codesystem-operation-lookup.html">CodeSystem/$lookup</a>
   */
  @Nonnull
  IOperationUntypedWithInput<Parameters> buildLookup(@Nonnull UriType system,
      @Nullable StringType version, @Nonnull CodeType code, @Nullable CodeType property);

  /**
   * Builds a new terminology client.
   *
   * @param fhirContext the FHIR context to use.
   * @param terminologyServerUrl the url to terminology server.
   * @param verboseRequestLogging enables verbose logging that includes bodes HTTP requests and
   * responses.
   * @param authConfig the authentication to use for the server.
   * @param httpClient the {@link HttpClient} instance to use for HTTP request.
   * @return the new instance of {@link TerminologyClient}.
   */
  static TerminologyClient build(@Nonnull final FhirContext fhirContext,
      @Nonnull final String terminologyServerUrl,
      final boolean verboseRequestLogging, @Nonnull final TerminologyAuthConfiguration authConfig,
      @Nonnull final HttpClient httpClient) {
    final IRestfulClientFactory restfulClientFactory = fhirContext.getRestfulClientFactory();
    restfulClientFactory.setHttpClient(httpClient);
    restfulClientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);

    final IGenericClient genericClient = restfulClientFactory.newGenericClient(
        terminologyServerUrl);

    genericClient.registerInterceptor(new UserAgentInterceptor());

    if (verboseRequestLogging) {
      final LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
      loggingInterceptor.setLogger(log);
      loggingInterceptor.setLogRequestSummary(true);
      loggingInterceptor.setLogResponseSummary(true);
      loggingInterceptor.setLogRequestHeaders(false);
      loggingInterceptor.setLogResponseHeaders(false);
      loggingInterceptor.setLogRequestBody(true);
      loggingInterceptor.setLogResponseBody(true);
      genericClient.registerInterceptor(loggingInterceptor);
    }

    if (authConfig.isEnabled()) {
      checkNotNull(authConfig.getTokenEndpoint());
      checkNotNull(authConfig.getClientId());
      checkNotNull(authConfig.getClientSecret());
      final ClientAuthInterceptor clientAuthInterceptor = new ClientAuthInterceptor(
          authConfig.getTokenEndpoint(), authConfig.getClientId(), authConfig.getClientSecret(),
          authConfig.getScope(), authConfig.getTokenExpiryTolerance());
      genericClient.registerInterceptor(clientAuthInterceptor);
    }
    return new DefaultTerminologyClient(genericClient);
  }

}
