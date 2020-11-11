/*
 * Copyright © 2018-2020, Commonwealth Scientific and Industrial Research
 * Organisation (CSIRO) ABN 41 687 119 230. Licensed under the CSIRO Open Source
 * Software Licence Agreement.
 */

package au.csiro.pathling.fhirpath;

import au.csiro.pathling.fhirpath.element.ElementDefinition;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.Getter;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.hl7.fhir.r4.model.Enumerations.ResourceType;

/**
 * Represents a path that is a collection of resources of more than one type.
 *
 * @author John Grimes
 */
public class UntypedResourcePath extends NonLiteralPath implements Referrer {

  /**
   * A {@link Column} within the dataset containing the resource type.
   */
  @Nonnull
  @Getter
  private final Column typeColumn;

  /**
   * A set of {@link ResourceType} objects that describe the different types that this collection
   * may contain.
   */
  @Nonnull
  @Getter
  private final Set<ResourceType> possibleTypes;

  private UntypedResourcePath(@Nonnull final String expression,
      @Nonnull final Dataset<Row> dataset, @Nonnull final Optional<Column> idColumn,
      @Nonnull final List<Column> valueColumns, final boolean singular,
      @Nonnull final Optional<List<Column>> thisColumns, @Nonnull final Column typeColumn,
      @Nonnull final Set<ResourceType> possibleTypes) {
    super(expression, dataset, idColumn, valueColumns, singular, Optional.empty(), thisColumns);
    this.typeColumn = typeColumn;
    this.possibleTypes = possibleTypes;
  }

  /**
   * @param expression The FHIRPath representation of this path
   * @param dataset A {@link Dataset} that can be used to evaluate this path against data
   * @param idColumn A {@link Column} within the dataset containing the identity of the subject
   * resource
   * @param valueColumn A {@link Column} within the dataset containing the values of the nodes
   * @param singular An indicator of whether this path represents a single-valued collection
   * @param thisColumns collection values where this path originated from {@code $this}
   * @param typeColumn A {@link Column} within the dataset containing the resource type
   * @param possibleTypes A set of {@link ResourceType} objects that describe the different types
   * @return a shiny new UntypedResourcePath
   */
  public static UntypedResourcePath build(@Nonnull final String expression,
      @Nonnull final Dataset<Row> dataset, @Nonnull final Optional<Column> idColumn,
      @Nonnull final Column valueColumn, final boolean singular,
      @Nonnull final Optional<List<Column>> thisColumns, @Nonnull final Column typeColumn,
      @Nonnull final Set<ResourceType> possibleTypes) {
    return new UntypedResourcePath(expression, dataset, idColumn,
        Collections.singletonList(valueColumn), singular, thisColumns, typeColumn, possibleTypes);
  }

  /**
   * @return a {@link Column} within the dataset containing the resource reference
   */
  @Nonnull
  public Column getValueColumn() {
    return valueColumns.get(0);
  }

  @Nonnull
  @Override
  public Optional<ElementDefinition> getChildElement(@Nonnull final String name) {
    return Optional.empty();
  }

  @Nonnull
  @Override
  public UntypedResourcePath copy(@Nonnull final String expression,
      @Nonnull final Dataset<Row> dataset, @Nonnull final Optional<Column> idColumn,
      @Nonnull final List<Column> valueColumns, final boolean singular,
      @Nonnull final Optional<List<Column>> thisColumns) {
    return new UntypedResourcePath(expression, dataset, idColumn, valueColumns, singular,
        thisColumns, typeColumn, possibleTypes);
  }

}
