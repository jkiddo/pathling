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

package au.csiro.pathling.terminology.lookup;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import au.csiro.pathling.fhir.ParametersUtils;
import au.csiro.pathling.fhir.TerminologyClient;
import au.csiro.pathling.fhirpath.encoding.ImmutableCoding;
import au.csiro.pathling.terminology.TerminologyOperation;
import au.csiro.pathling.terminology.TerminologyParameters;
import au.csiro.pathling.terminology.TerminologyService.Designation;
import au.csiro.pathling.terminology.TerminologyService.Property;
import au.csiro.pathling.terminology.TerminologyService.PropertyOrDesignation;
import au.csiro.pathling.terminology.caching.CacheableListCollector;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;

/**
 * An implementation of {@link TerminologyOperation} for the lookup operation.
 *
 * @author John Grimes
 * @see <a
 * href="https://www.hl7.org/fhir/R4/codesystem-operation-lookup.html">CodeSystem/$lookup</a>
 */
public class LookupExecutor implements
    TerminologyOperation<Parameters, ArrayList<PropertyOrDesignation>> {

  @Nonnull
  private final TerminologyClient terminologyClient;

  @Nonnull
  private final LookupParameters parameters;

  public LookupExecutor(@Nonnull final TerminologyClient terminologyClient,
      @Nonnull final LookupParameters parameters) {
    this.terminologyClient = terminologyClient;
    this.parameters = parameters;
  }

  @Override
  @Nonnull
  public Optional<ArrayList<PropertyOrDesignation>> validate() {
    final ImmutableCoding coding = parameters.getCoding();

    // If the system or the code of the coding is null, the result is an empty list.
    if (isNull(coding.getSystem()) || isNull(coding.getCode())) {
      return Optional.of(new ArrayList<>());
    } else {
      return Optional.empty();
    }
  }

  @Override
  @Nonnull
  public IOperationUntypedWithInput<Parameters> buildRequest() {
    final ImmutableCoding coding = parameters.getCoding();
    return terminologyClient.buildLookup(
        TerminologyParameters.required(UriType::new, coding.getSystem()),
        TerminologyParameters.optional(StringType::new, coding.getVersion()),
        TerminologyParameters.required(CodeType::new, coding.getCode()),
        TerminologyParameters.optional(CodeType::new, parameters.getProperty()),
        TerminologyParameters.optional(StringType::new, parameters.getAcceptLanguage())
    );
  }

  @Override
  @Nonnull
  public ArrayList<PropertyOrDesignation> extractResult(@Nonnull final Parameters response) {
    return toPropertiesAndDesignations(response, parameters.getProperty());
  }

  @Override
  @Nonnull
  public ArrayList<PropertyOrDesignation> invalidRequestFallback() {
    return new ArrayList<>();
  }

  @Nonnull
  private static ArrayList<PropertyOrDesignation> toPropertiesAndDesignations(
      @Nonnull final Parameters parameters,
      @Nullable final String propertyCode) {

    return (Designation.PROPERTY_CODE.equals(propertyCode))
           ? toDesignations(parameters)
           : toProperties(parameters, propertyCode);
  }

  @Nonnull
  private static ArrayList<PropertyOrDesignation> toDesignations(
      @Nonnull final Parameters parameters) {
    return ParametersUtils.toDesignations(parameters)
        .map(Designation::ofPart)
        .collect(new CacheableListCollector<>());
  }

  @Nonnull
  private static ArrayList<PropertyOrDesignation> toProperties(
      @Nonnull final Parameters parameters, @Nullable final String propertyCode) {
    return ParametersUtils.toProperties(parameters)
        .flatMap(part -> nonNull(part.getSubproperty())
                         ? part.getSubproperty().stream()
                         : Stream.of(part))
        .map(part -> Property.of(part.getCode().getValue(), requireNonNull(part.getValue())))
        .filter(property -> isNull(propertyCode) || propertyCode.equals(property.getCode()))
        .collect(new CacheableListCollector<>());
  }

}
