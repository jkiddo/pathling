/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.test.builders;

import static org.apache.spark.sql.functions.col;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import au.csiro.pathling.fhirpath.UntypedResourcePath;
import au.csiro.pathling.fhirpath.element.ReferencePath;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.hl7.fhir.r4.model.Enumerations.ResourceType;

/**
 * @author John Grimes
 */
@SuppressWarnings("unused")
public class UntypedResourcePathBuilder {

  @Nonnull
  private String expression;

  @Nonnull
  private Dataset<Row> dataset;

  @Nonnull
  private Column idColumn;

  @Nullable
  private Column eidColumn;

  @Nonnull
  private Column valueColumn;

  private boolean singular;

  @Nullable
  private Column thisColumn;

  @Nonnull
  private Column typeColumn;

  @Nonnull
  private Set<ResourceType> possibleTypes;

  public UntypedResourcePathBuilder() {
    expression = "";
    dataset = new DatasetBuilder()
        .withIdColumn()
        .withColumn(DataTypes.StringType)
        .withColumn(DataTypes.StringType)
        .build();
    idColumn = col(dataset.columns()[0]);
    valueColumn = col(dataset.columns()[1]);
    typeColumn = col(dataset.columns()[2]);
    singular = false;
    possibleTypes = Collections.emptySet();
  }

  @Nonnull
  public UntypedResourcePathBuilder idTypeAndValueColumns() {
    idColumn = functions.col(dataset.columns()[0]);
    typeColumn = functions.col(dataset.columns()[1]);
    valueColumn = functions.col(dataset.columns()[2]);
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder expression(@Nonnull final String expression) {
    this.expression = expression;
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder dataset(@Nonnull final Dataset<Row> dataset) {
    this.dataset = dataset;
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder idColumn(@Nonnull final Column idColumn) {
    this.idColumn = idColumn;
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder eidColumn(@Nonnull final Column eidColumn) {
    this.eidColumn = eidColumn;
    return this;
  }


  @Nonnull
  public UntypedResourcePathBuilder valueColumn(@Nonnull final Column valueColumn) {
    this.valueColumn = valueColumn;
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder singular(final boolean singular) {
    this.singular = singular;
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder thisColumn(@Nonnull final Column thisColumn) {
    this.thisColumn = thisColumn;
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder typeColumn(@Nonnull final Column typeColumn) {
    this.typeColumn = typeColumn;
    return this;
  }

  @Nonnull
  public UntypedResourcePathBuilder possibleTypes(@Nonnull final Set<ResourceType> possibleTypes) {
    this.possibleTypes = possibleTypes;
    return this;
  }

  @Nonnull
  public UntypedResourcePath build() {
    final ReferencePath referencePath = mock(ReferencePath.class);
    when(referencePath.getValueColumn()).thenReturn(valueColumn);
    when(referencePath.isSingular()).thenReturn(singular);
    when(referencePath.getThisColumn()).thenReturn(Optional.ofNullable(thisColumn));
    return UntypedResourcePath
        .build(referencePath, expression, dataset, idColumn, Optional.ofNullable(eidColumn),
            typeColumn, possibleTypes);
  }

}
