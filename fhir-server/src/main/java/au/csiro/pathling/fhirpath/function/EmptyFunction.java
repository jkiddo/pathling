/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.fhirpath.function;

import static au.csiro.pathling.fhirpath.function.NamedFunction.checkNoArguments;
import static au.csiro.pathling.fhirpath.function.NamedFunction.expressionFromInput;
import static org.apache.spark.sql.functions.coalesce;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.when;

import au.csiro.pathling.fhirpath.FhirPath;
import au.csiro.pathling.fhirpath.NonLiteralPath;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.apache.spark.sql.Column;
import org.hl7.fhir.r4.model.Enumerations.FHIRDefinedType;

/**
 * This function returns true if the input collection is empty.
 *
 * @author John Grimes
 * @see <a href="https://pathling.csiro.au/docs/fhirpath/functions.html#empty">empty</a>
 */
public class EmptyFunction extends AggregateFunction implements NamedFunction {

  private static final String NAME = "empty";

  @Nonnull
  @Override
  public FhirPath invoke(@Nonnull final NamedFunctionInput input) {
    checkNoArguments(NAME, input);
    final NonLiteralPath inputPath = input.getInput();
    final String expression = expressionFromInput(input, "empty");

    // "Empty" means that the group contains only null values.
    final Function<Column, Column> empty = col -> when(count(col).equalTo(0), true)
        .otherwise(false);

    // If we encounter a path with multiple value columns (e.g. a ResourcePath), we use coalesce to 
    // convert it to a single value, null or otherwise.
    final Column inputColumn = inputPath.getValueColumns().size() == 1
                               ? inputPath.getValueColumns().get(0)
                               : coalesce(inputPath.getValueColumns().toArray(new Column[0]));

    return applyAggregationFunction(input.getContext(), inputPath, inputColumn, empty, expression,
        FHIRDefinedType.BOOLEAN);
  }

}
