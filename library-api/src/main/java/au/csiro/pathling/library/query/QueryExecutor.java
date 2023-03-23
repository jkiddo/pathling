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

package au.csiro.pathling.library.query;

import au.csiro.pathling.aggregate.AggregateRequest;
import au.csiro.pathling.extract.ExtractRequest;
import javax.annotation.Nonnull;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Abstract class that encapsulates the logic for executing queries in this package.
 * 
 * @author Piotr Szul
 */
public abstract class QueryExecutor {

  /**
   * Executes the given extract request.
   *
   * @param extractRequest the request to execute.
   * @return the result of the execution.
   */
  @Nonnull
  abstract protected Dataset<Row> execute(@Nonnull final ExtractRequest extractRequest);

  /**
   * Executes the given aggregate request.
   *
   * @param aggregateRequest the request to execute.
   * @return the result of the execution.
   */
  @Nonnull
  abstract protected Dataset<Row> execute(@Nonnull final AggregateRequest aggregateRequest);
}
