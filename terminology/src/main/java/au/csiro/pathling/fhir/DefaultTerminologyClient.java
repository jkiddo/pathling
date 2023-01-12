/*
 * Copyright 2023 Commonwealth Scientific and Industrial Research
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

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;

/**
 * An implementation of {@link TerminologyClient} that uses cacheable GET requests.
 */
class DefaultTerminologyClient implements TerminologyClient {

  @Nonnull
  final IGenericClient fhirClient;

  DefaultTerminologyClient(@Nonnull final IGenericClient fhirClient) {
    this.fhirClient = fhirClient;
  }

  @Nonnull
  @Override
  public Parameters validateCode(@Nonnull final UriType url, @Nonnull final UriType system,
      @Nullable final StringType version, @Nonnull final CodeType code) {
    return buildValidateCode(url, system, version, code).execute();
  }

  @Nonnull
  @Override
  public IOperationUntypedWithInput<Parameters> buildValidateCode(@Nonnull final UriType url,
      @Nonnull final UriType system, @Nullable final StringType version,
      @Nonnull final CodeType code) {
    final Parameters params = new Parameters();
    params.addParameter().setName("url").setValue(url);
    params.addParameter().setName("system").setValue(system);
    params.addParameter().setName("code").setValue(code);
    if (version != null) {
      params.addParameter().setName("systemVersion").setValue(version);
    }

    return fhirClient.operation()
        .onType(ValueSet.class)
        .named("$validate-code")
        .withParameters(params)
        .useHttpGet();
  }

  @Nonnull
  @Override
  public Parameters translate(@Nonnull final UriType url, @Nonnull final UriType system,
      @Nullable final StringType version, @Nonnull final CodeType code,
      @Nullable final BooleanType reverse, @Nullable final UriType target) {
    return buildTranslate(url, system, version, code, reverse, target).execute();
  }

  @Nonnull
  @Override
  public IOperationUntypedWithInput<Parameters> buildTranslate(@Nonnull final UriType url,
      @Nonnull final UriType system, @Nullable final StringType version,
      @Nonnull final CodeType code, @Nullable final BooleanType reverse,
      @Nullable final UriType target) {
    final Parameters params = new Parameters();
    params.addParameter().setName("url").setValue(url);
    params.addParameter().setName("system").setValue(system);
    params.addParameter().setName("code").setValue(code);
    if (version != null) {
      params.addParameter().setName("version").setValue(version);
    }
    if (reverse != null) {
      params.addParameter().setName("reverse").setValue(reverse);
    }
    if (target != null) {
      params.addParameter().setName("target").setValue(target);
    }
    return fhirClient.operation()
        .onType(ConceptMap.class)
        .named("$translate")
        .withParameters(params)
        .useHttpGet();
  }

  @Nonnull
  @Override
  public Parameters subsumes(@Nonnull final CodeType codeA, @Nonnull final CodeType codeB,
      @Nonnull final UriType system, @Nullable final StringType version) {
    return buildSubsumes(codeA, codeB, system, version).execute();
  }

  @Nonnull
  @Override
  public IOperationUntypedWithInput<Parameters> buildSubsumes(@Nonnull final CodeType codeA,
      @Nonnull final CodeType codeB, @Nonnull final UriType system,
      @Nullable final StringType version) {
    final Parameters params = new Parameters();
    params.addParameter().setName("codeA").setValue(codeA);
    params.addParameter().setName("codeB").setValue(codeB);
    params.addParameter().setName("system").setValue(system);
    if (version != null) {
      params.addParameter().setName("version").setValue(version);
    }
    return fhirClient.operation()
        .onType(CodeSystem.class)
        .named("$subsumes")
        .withParameters(params)
        .useHttpGet();
  }

  @Nonnull
  @Override
  public Parameters lookup(@Nonnull final UriType system,
      @Nullable final StringType version, @Nonnull final CodeType code,
      @Nullable final CodeType property) {
    return buildLookup(system, version, code, property).execute();
  }

  @Nonnull
  @Override
  public IOperationUntypedWithInput<Parameters> buildLookup(@Nonnull final UriType system,
      @Nullable final StringType version,
      @Nonnull final CodeType code, @Nullable final CodeType property) {
    final Parameters params = new Parameters();
    params.addParameter().setName("system").setValue(system);
    params.addParameter().setName("code").setValue(code);
    if (version != null) {
      params.addParameter().setName("version").setValue(version);
    }
    if (property != null) {
      params.addParameter().setName("property").setValue(property);
    }
    return fhirClient.operation()
        .onType(CodeSystem.class)
        .named("$lookup")
        .withParameters(params)
        .useHttpGet();
  }

}