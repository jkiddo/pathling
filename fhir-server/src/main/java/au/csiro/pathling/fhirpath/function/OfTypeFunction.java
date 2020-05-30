/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.fhirpath.function;

import static au.csiro.pathling.QueryHelpers.joinOnId;
import static au.csiro.pathling.utilities.Preconditions.checkUserInput;

import au.csiro.pathling.QueryHelpers.JoinType;
import au.csiro.pathling.fhirpath.FhirPath;
import au.csiro.pathling.fhirpath.ResourcePath;
import au.csiro.pathling.fhirpath.UntypedResourcePath;
import javax.annotation.Nonnull;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * A function filters items in the input collection to only those that are of the given type.
 *
 * @author John Grimes
 * @see <a href="https://pathling.csiro.au/docs/fhirpath/functions.html#oftype">ofType</a>
 */
public class OfTypeFunction implements NamedFunction {

  private static final String NAME = "ofType";

  @Nonnull
  @Override
  public FhirPath invoke(@Nonnull final NamedFunctionInput input) {
    final FhirPath inputPath = input.getInput();
    final String expression = NamedFunction.expressionFromInput(input, NAME);
    checkUserInput(inputPath instanceof UntypedResourcePath,
        "Input to ofType function must be a polymorphic resource type: " + inputPath
            .getExpression());
    checkUserInput(input.getArguments().size() == 1,
        "ofType function must have one argument: " + expression);
    final FhirPath argumentPath = input.getArguments().get(0);

    // If the input is a polymorphic resource reference, check that the argument is a resource 
    // type.
    checkUserInput(argumentPath instanceof ResourcePath,
        "Argument to ofType function must be a resource type: " + expression);
    final ResourcePath resourcePath = (ResourcePath) argumentPath;

    // Do a left outer join to the resource dataset using the reference in the untyped dataset - the
    // result will be null in the rows that are not of the resource type nominated.
    final Dataset<Row> inputDataset = inputPath.getDataset();
    final Column inputReference = inputPath.getValueColumn().getField("reference");
    final Dataset<Row> dataset = joinOnId(inputDataset, inputReference, resourcePath,
        JoinType.LEFT_OUTER);

    // Return a new resource path with the joined dataset, and the argument's value column.
    return new ResourcePath(expression, dataset, inputPath.getIdColumn(),
        resourcePath.getValueColumn(), inputPath.isSingular(), resourcePath.getDefinition());
  }

}
