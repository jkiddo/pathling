/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.fhirpath;

import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Represents any FHIRPath expression - all expressions implement this interface.
 *
 * @author John Grimes
 */
public interface FhirPath {

  /**
   * @return the FHIRPath expression that represents this path
   */
  @Nonnull
  String getExpression();

  /**
   * @return the {@link Dataset} that can be used to evaluate this path against data
   */
  @Nonnull
  Dataset<Row> getDataset();

  /**
   * Gets an ID {@link Column} from any of the inputs, if there is one.
   *
   * @param inputs a collection of objects
   * @return a {@link Column}, if one was found
   */
  @Nonnull
  static Column findIdColumn(@Nonnull final Object... inputs) {
    return Stream.of(inputs)
        .filter(path -> path instanceof FhirPath)
        .map(path -> (FhirPath) path)
        .findFirst()
        .orElseThrow()
        .getIdColumn();
  }

  /**
   * @return a {@link Column} within the dataset containing the identity of the subject resource
   */
  @Nonnull
  Column getIdColumn();

  /**
   * @return an indicator of whether this path represents a single-valued collection
   */
  boolean isSingular();

  /**
   * @return a {@link Column} within the dataset containing the values of the nodes
   */
  @Nonnull
  Column getValueColumn();

}
