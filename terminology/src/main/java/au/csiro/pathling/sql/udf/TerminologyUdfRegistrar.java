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

package au.csiro.pathling.sql.udf;

import au.csiro.pathling.terminology.TerminologyServiceFactory;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.apache.spark.sql.SparkSession;

/**
 * The {@link SqlFunctionRegistrar} for terminology UDFs.
 */
public class TerminologyUdfRegistrar extends SqlFunctionRegistrar {

  public TerminologyUdfRegistrar(@Nonnull TerminologyServiceFactory tsf) {
    super(List.of(new DisplayUdf(tsf)),
        List.of(new MemberOfUdf(tsf)),
        List.of(new SubsumesUdf(tsf)),
        Collections.emptyList(),
        List.of(new TranslateUdf(tsf)));
  }

  /**
   * Registers terminology UDFs in provided spark session.
   *
   * @param spark the session to configure.
   * @param terminologyServiceFactory the {@link TerminologyServiceFactory} to use for the UDFs.
   */
  public static void registerUdfs(@Nonnull final SparkSession spark,
      @Nonnull final TerminologyServiceFactory terminologyServiceFactory) {
    new TerminologyUdfRegistrar(terminologyServiceFactory).configure(spark);
  }
}
