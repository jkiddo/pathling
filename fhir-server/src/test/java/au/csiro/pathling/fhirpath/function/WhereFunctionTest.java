/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.fhirpath.function;

import static au.csiro.pathling.test.assertions.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import au.csiro.pathling.errors.InvalidUserInputError;
import au.csiro.pathling.fhirpath.FhirPath;
import au.csiro.pathling.fhirpath.ResourcePath;
import au.csiro.pathling.fhirpath.element.ElementPath;
import au.csiro.pathling.fhirpath.literal.BooleanLiteralPath;
import au.csiro.pathling.fhirpath.parser.ParserContext;
import au.csiro.pathling.test.builders.DatasetBuilder;
import au.csiro.pathling.test.builders.ElementPathBuilder;
import au.csiro.pathling.test.builders.ParserContextBuilder;
import au.csiro.pathling.test.builders.ResourcePathBuilder;
import java.util.Arrays;
import java.util.Collections;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.hl7.fhir.r4.model.Enumerations.FHIRDefinedType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author John Grimes
 */
@Tag("UnitTest")
public class WhereFunctionTest {

  // This test simulates the execution of the where function on the path
  // `Patient.reverseResolve(Encounter.subject).where($this.status = 'in-progress')`.
  @Test
  public void whereOnResource() {
    final Dataset<Row> inputDataset = new DatasetBuilder()
        .withIdColumn()
        .withIdColumn("id")
        .withColumn("status", DataTypes.StringType)
        .withRow("Patient/abc1", "Encounter/xyz1", "in-progress")
        .withRow("Patient/abc1", "Encounter/xyz2", "finished")
        .withRow("Patient/abc2", "Encounter/xyz3", "in-progress")
        .withRow("Patient/abc3", "Encounter/xyz4", "in-progress")
        .withRow("Patient/abc3", "Encounter/xyz5", "finished")
        .withRow("Patient/abc4", "Encounter/xyz6", "finished")
        .withRow("Patient/abc4", "Encounter/xyz7", "finished")
        .withRow("Patient/abc5", null, null)
        .build();
    final Column idColumn = inputDataset.col(inputDataset.columns()[0]);

    final ResourcePath inputPath = new ResourcePathBuilder()
        .expression("reverseResolve(Encounter.subject)")
        .dataset(inputDataset)
        .idColumn(idColumn)
        .valueColumn(idColumn)
        .buildCustom();

    // Build an expression which represents the argument to the function. We assume that the value
    // column from the input dataset is also present within the argument dataset.
    final Dataset<Row> argumentDataset = inputPath.getDataset()
        .withColumn("value", inputPath.getDataset().col("status").equalTo("in-progress"));
    final ElementPath argumentPath = new ElementPathBuilder()
        .fhirType(FHIRDefinedType.BOOLEAN)
        .dataset(argumentDataset)
        .idColumn(inputPath.getIdColumn())
        .valueColumn(argumentDataset.col("value"))
        .thisColumn(inputPath.getValueColumn())
        .singular(true)
        .build();

    // Prepare the input to the function.
    final ParserContext parserContext = new ParserContextBuilder().build();
    final NamedFunctionInput whereInput = new NamedFunctionInput(parserContext,
        inputPath, Collections.singletonList(argumentPath));

    // Execute the function.
    final NamedFunction whereFunction = NamedFunction.getInstance("where");
    final FhirPath result = whereFunction.invoke(whereInput);

    // Check the result dataset.
    final Dataset<Row> expectedDataset = new DatasetBuilder()
        .withIdColumn()
        .withColumn("id", DataTypes.StringType)
        .withColumn("status", DataTypes.StringType)
        .withRow("Patient/abc1", "Encounter/xyz1", "in-progress")
        .withRow("Patient/abc2", "Encounter/xyz3", "in-progress")
        .withRow("Patient/abc3", "Encounter/xyz4", "in-progress")
        .build();
    assertThat(result)
        .selectResult()
        .hasRows(expectedDataset);
  }

  @Test
  public void whereOnElement() {
    // Build an expression which represents the input to the function.
    final Dataset<Row> dataset = new DatasetBuilder()
        .withIdColumn()
        .withValueColumn(DataTypes.StringType)
        .withRow("Patient/abc1", "en")
        .withRow("Patient/abc1", "es")
        .withRow("Patient/abc2", "de")
        .withRow("Patient/abc3", "en")
        .withRow("Patient/abc3", "en")
        .withRow("Patient/abc3", "zh")
        .withRow("Patient/abc4", "fr")
        .withRow("Patient/abc4", "fr")
        .withRow("Patient/abc5", null)
        .build();
    final ElementPath inputPath = new ElementPathBuilder()
        .fhirType(FHIRDefinedType.STRING)
        .dataset(dataset)
        .idAndValueColumns()
        .singular(false)
        .build();

    // Build an expression which represents the argument to the function.
    final Dataset<Row> argumentDataset = inputPath.getDataset()
        .withColumn("value", inputPath.getValueColumn().equalTo("en"));
    final ElementPath argumentExpression = new ElementPathBuilder()
        .fhirType(FHIRDefinedType.BOOLEAN)
        .dataset(argumentDataset)
        .idColumn(inputPath.getIdColumn())
        .valueColumn(argumentDataset.col("value"))
        .thisColumn(inputPath.getValueColumn())
        .singular(true)
        .build();

    // Prepare the input to the function.
    final ParserContext parserContext = new ParserContextBuilder().build();
    final NamedFunctionInput whereInput = new NamedFunctionInput(parserContext,
        inputPath,
        Collections.singletonList(argumentExpression));

    // Execute the function.
    final NamedFunction whereFunction = NamedFunction.getInstance("where");
    final FhirPath result = whereFunction.invoke(whereInput);

    // Check the result dataset.
    final Dataset<Row> expectedDataset = new DatasetBuilder()
        .withIdColumn()
        .withValueColumn(DataTypes.StringType)
        .withRow("Patient/abc1", "en")
        .withRow("Patient/abc3", "en")
        .withRow("Patient/abc3", "en")
        .build();
    assertThat(result)
        .selectResult()
        .hasRows(expectedDataset);
  }

  @Test
  public void nullValuesAreNull() {
    // Build an expression which represents the input to the function.
    final Dataset<Row> dataset = new DatasetBuilder()
        .withIdColumn()
        .withValueColumn(DataTypes.StringType)
        .withRow("Patient/abc1", "en")
        .withRow("Patient/abc1", "es")
        .withRow("Patient/abc2", "de")
        .withRow("Patient/abc3", "en")
        .withRow("Patient/abc3", "en")
        .withRow("Patient/abc3", "zh")
        .withRow("Patient/abc4", "ar")
        .build();
    final ElementPath inputPath = new ElementPathBuilder()
        .fhirType(FHIRDefinedType.STRING)
        .dataset(dataset)
        .idAndValueColumns()
        .singular(false)
        .build();

    // Build an expression which represents the argument to the function.
    final Dataset<Row> argumentDataset = inputPath.getDataset()
        .withColumn("value",
            functions.when(inputPath.getValueColumn().equalTo("en"), null).otherwise(true));
    final ElementPath argumentPath = new ElementPathBuilder()
        .fhirType(FHIRDefinedType.BOOLEAN)
        .dataset(argumentDataset)
        .idColumn(inputPath.getIdColumn())
        .valueColumn(argumentDataset.col("value"))
        .thisColumn(inputPath.getValueColumn())
        .singular(true)
        .build();

    // Prepare the input to the function.
    final ParserContext parserContext = new ParserContextBuilder().build();
    final NamedFunctionInput whereInput = new NamedFunctionInput(parserContext,
        inputPath, Collections.singletonList(argumentPath));

    // Execute the function.
    final NamedFunction whereFunction = NamedFunction.getInstance("where");
    final FhirPath result = whereFunction.invoke(whereInput);

    // Check the result dataset.
    final Dataset<Row> expectedDataset = new DatasetBuilder()
        .withIdColumn()
        .withValueColumn(DataTypes.StringType)
        .withRow("Patient/abc1", "es")
        .withRow("Patient/abc2", "de")
        .withRow("Patient/abc3", "zh")
        .withRow("Patient/abc4", "ar")
        .build();
    assertThat(result)
        .selectResult()
        .hasRows(expectedDataset);
  }

  @Test
  public void throwsErrorIfMoreThanOneArgument() {
    final ResourcePath input = new ResourcePathBuilder().build();
    final ElementPath argument1 = new ElementPathBuilder()
        .expression("$this.gender = 'female'")
        .fhirType(FHIRDefinedType.BOOLEAN)
        .build();
    final ElementPath argument2 = new ElementPathBuilder()
        .expression("$this.gender != 'male'")
        .fhirType(FHIRDefinedType.BOOLEAN)
        .build();

    final ParserContext parserContext = new ParserContextBuilder().build();
    final NamedFunctionInput whereInput = new NamedFunctionInput(parserContext, input,
        Arrays.asList(argument1, argument2));

    final NamedFunction whereFunction = NamedFunction.getInstance("where");
    final InvalidUserInputError error = assertThrows(
        InvalidUserInputError.class,
        () -> whereFunction.invoke(whereInput));
    assertEquals("where function accepts one argument", error.getMessage());
  }

  @Test
  public void throwsErrorIfArgumentNotBoolean() {
    final ResourcePath input = new ResourcePathBuilder().build();
    final ElementPath argument = new ElementPathBuilder()
        .expression("$this.gender")
        .fhirType(FHIRDefinedType.STRING)
        .build();

    final ParserContext parserContext = new ParserContextBuilder().build();
    final NamedFunctionInput whereInput = new NamedFunctionInput(parserContext, input,
        Collections.singletonList(argument));

    final NamedFunction whereFunction = NamedFunction.getInstance("where");
    final InvalidUserInputError error = assertThrows(
        InvalidUserInputError.class,
        () -> whereFunction.invoke(whereInput));
    assertEquals(
        "Argument to where function must be a singular Boolean: $this.gender",
        error.getMessage());
  }

  @Test
  public void throwsErrorIfArgumentNotSingular() {
    final ResourcePath input = new ResourcePathBuilder().build();
    final ElementPath argument = new ElementPathBuilder()
        .expression("$this.communication.preferred")
        .fhirType(FHIRDefinedType.BOOLEAN)
        .singular(false)
        .build();

    final ParserContext parserContext = new ParserContextBuilder().build();
    final NamedFunctionInput whereInput = new NamedFunctionInput(parserContext, input,
        Collections.singletonList(argument));

    final NamedFunction whereFunction = NamedFunction.getInstance("where");
    final InvalidUserInputError error = assertThrows(
        InvalidUserInputError.class,
        () -> whereFunction.invoke(whereInput));
    assertEquals(
        "Argument to where function must be a singular Boolean: $this.communication.preferred",
        error.getMessage());
  }

  @Test
  public void throwsErrorIfArgumentIsLiteral() {
    final ResourcePath input = new ResourcePathBuilder().build();
    final BooleanLiteralPath argument = BooleanLiteralPath
        .fromString("true", input);

    final ParserContext parserContext = new ParserContextBuilder().build();
    final NamedFunctionInput whereInput = new NamedFunctionInput(parserContext, input,
        Collections.singletonList(argument));

    final NamedFunction whereFunction = NamedFunction.getInstance("where");
    final InvalidUserInputError error = assertThrows(
        InvalidUserInputError.class,
        () -> whereFunction.invoke(whereInput));
    assertEquals(
        "Argument to where function cannot be a literal: true",
        error.getMessage());
  }

}