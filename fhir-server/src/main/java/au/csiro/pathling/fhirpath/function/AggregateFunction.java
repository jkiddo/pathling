/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.fhirpath.function;

import static au.csiro.pathling.QueryHelpers.aliasColumn;
import static org.apache.spark.sql.functions.row_number;

import au.csiro.pathling.QueryHelpers.DatasetWithColumn;
import au.csiro.pathling.fhirpath.FhirPath;
import au.csiro.pathling.fhirpath.NonLiteralPath;
import au.csiro.pathling.fhirpath.element.ElementPath;
import au.csiro.pathling.fhirpath.parser.ParserContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.hl7.fhir.r4.model.Enumerations.FHIRDefinedType;

/**
 * Represents a function intended to reduce a set of values to a single value.
 *
 * @author John Grimes
 */
public abstract class AggregateFunction {

  @Nonnull
  protected static WindowSpec getWindowSpec(@Nonnull final ParserContext context) {
    final Column[] groupingColumns = context.getGroupingColumns().toArray(new Column[0]);
    final Column idColumn = context.getInputContext().getIdColumn();
    return (groupingColumns.length > 0
            ? Window.partitionBy(groupingColumns).orderBy(groupingColumns)
            : Window.partitionBy(idColumn).orderBy(idColumn));
  }

  /**
   * Builds a result for an aggregation operation, with a single {@link FhirPath} object as input
   * that will be copied and used as a template for the new result.
   *
   * @param dataset the {@link Dataset} that will be used in the result
   * @param input the {@link FhirPath} objects being aggregated
   * @param valueColumn a {@link Column} describing the resulting value
   * @param expression the FHIRPath expression for the result
   * @return a new {@link ElementPath} representing the result
   */
  @Nonnull
  protected NonLiteralPath buildResult(@Nonnull final Dataset<Row> dataset,
      @Nonnull final WindowSpec window, @Nonnull final NonLiteralPath input,
      @Nonnull final Column valueColumn, @Nonnull final String expression) {

    return buildResult(dataset, window, Collections.singletonList(input), valueColumn, expression,
        input::copy);
  }

  /**
   * Builds a result for an aggregation operation, with a single {@link FhirPath} object as input.
   *
   * @param dataset the {@link Dataset} that will be used in the result
   * @param input the {@link FhirPath} objects being aggregated
   * @param valueColumn a {@link Column} describing the resulting value
   * @param expression the FHIRPath expression for the result
   * @param fhirType the {@link FHIRDefinedType} of the result
   * @return a new {@link ElementPath} representing the result
   */
  @Nonnull
  protected ElementPath buildResult(@Nonnull final Dataset<Row> dataset,
      @Nonnull final WindowSpec window, @Nonnull final FhirPath input,
      @Nonnull final Column valueColumn, @Nonnull final String expression,
      @Nonnull final FHIRDefinedType fhirType) {

    return buildResult(dataset, window, Collections.singletonList(input), valueColumn, expression,
        fhirType);
  }

  /**
   * Builds a result for an aggregation operation, with possibly multiple {@link FhirPath} objects
   * as input (e.g. in the case of a binary operator that performs aggregation).
   *
   * @param dataset the {@link Dataset} that will be used in the result
   * @param inputs the {@link FhirPath} objects being aggregated
   * @param valueColumn a {@link Column} describing the resulting value
   * @param expression the FHIRPath expression for the result
   * @param fhirType the {@link FHIRDefinedType} of the result
   * @return a new {@link ElementPath} representing the result
   */
  @Nonnull
  protected ElementPath buildResult(@Nonnull final Dataset<Row> dataset,
      @Nonnull final WindowSpec window, @Nonnull final Collection<FhirPath> inputs,
      @Nonnull final Column valueColumn, @Nonnull final String expression,
      @Nonnull final FHIRDefinedType fhirType) {

    return buildResult(dataset, window, inputs, valueColumn, expression,
        // create the result as an ElementPath of given FhirType
        (exp, ds, id, value, singular, thisColumn) -> ElementPath
            .build(exp, ds, id, value, true, Optional.empty(), thisColumn, fhirType));
  }

  @Nonnull
  private <T extends FhirPath> T buildResult(@Nonnull final Dataset<Row> dataset,
      @Nonnull final WindowSpec window, @Nonnull final Collection<FhirPath> inputs,
      @Nonnull final Column valueColumn, @Nonnull final String expression,
      @Nonnull final ResultPathFactory<T> resultPathFactory) {

    // Use an ID column from any of the inputs.
    final Column idColumn = FhirPath.findIdColumn(inputs.toArray());

    // Get any this columns that may be present in the inputs.
    final Optional<Column> thisColumn = NonLiteralPath.findThisColumn(inputs);

    // Filter the result to contain a single row for each partition.
    final DatasetWithColumn datasetWithColumn = aliasColumn(dataset,
        row_number().over(window).equalTo(1));
    final Dataset<Row> finalDataset = datasetWithColumn.getDataset()
        .filter(datasetWithColumn.getColumn());

    return resultPathFactory
        .create(expression, finalDataset, idColumn, valueColumn, true, thisColumn);
  }

  /**
   * A factory that encapsulates creation of the aggregation result path.
   *
   * @param <T> subtype of FhirPath to create
   */
  private interface ResultPathFactory<T extends FhirPath> {

    /**
     * Creates a subtype T of FhirPath
     *
     * @param expression an updated expression to describe the new FhirPath
     * @param dataset the new Dataset that can be used to evaluate this FhirPath against data
     * @param idColumn the new resource identity column
     * @param valueColumn the new expression value column
     * @param singular the new singular value
     * @param thisColumn a column containing the collection being iterated, for cases where a path
     * is being created to represent the {@code $this} keyword
     * @return a new instance of T
     */
    T create(@Nonnull String expression, @Nonnull Dataset<Row> dataset,
        @Nonnull Column idColumn, @Nonnull Column valueColumn, boolean singular,
        @Nonnull Optional<Column> thisColumn);
  }

}
