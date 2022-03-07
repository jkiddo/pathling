/*
 * This is a modified version of the Bunsen library, originally published at
 * https://github.com/cerner/bunsen.
 *
 * Bunsen is copyright 2017 Cerner Innovation, Inc., and is licensed under
 * the Apache License, version 2.0 (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * These modifications are copyright © 2018-2022, Commonwealth Scientific
 * and Industrial Research Organisation (CSIRO) ABN 41 687 119 230. Licensed
 * under the CSIRO Open Source Software Licence Agreement.
 *
 */

package au.csiro.pathling.encoders2

import au.csiro.pathling.encoders.datatypes.DataTypeMappings
import au.csiro.pathling.encoders.{EncoderConfig, EncoderContext, SchemaConverter}
import au.csiro.pathling.encoders2.ExtensionSupport.{EXTENSIONS_FIELD_NAME, FID_FIELD_NAME}
import au.csiro.pathling.encoders2.SchemaVisitor.isCollection
import ca.uhn.fhir.context._
import org.apache.spark.sql.types._
import org.hl7.fhir.instance.model.api.IBase

/**
 * The schema processor for converting FHIR schemas to SQL schemas.
 *
 * @param fhirContext      the FHIR context to use.
 * @param dataTypeMappings data type mappings to use.
 * @param config           encoder configuration to use.
 */
private[encoders2] class SchemaConverterProcessor(override val fhirContext: FhirContext,
                                                  override val dataTypeMappings: DataTypeMappings,
                                                  override val config: EncoderConfig) extends
  SchemaProcessorWithTypeMappings[DataType, StructField] {

  private def createExtensionField(definition: BaseRuntimeElementCompositeDefinition[_]): Seq[StructField] = {
    // For resources, also add _extension.
    definition match {
      case _: RuntimeResourceDefinition if supportsExtensions =>
        val extensionSchema = buildExtensionValue()
        StructField(EXTENSIONS_FIELD_NAME, MapType(IntegerType, extensionSchema, valueContainsNull = false)) :: Nil
      case _ => Nil
    }
  }

  private def createFidField(): Seq[StructField] = {
    // For each composite, add _fid.
    if (generateFid) {
      StructField(FID_FIELD_NAME, IntegerType) :: Nil
    } else {
      Nil
    }
  }

  override def buildComposite(definition: BaseRuntimeElementCompositeDefinition[_], fields: Seq[StructField]): DataType = {
    StructType(fields ++ createFidField() ++ createExtensionField(definition))
  }

  override def buildElement(elementName: String, elementValue: DataType, elementDefinition: BaseRuntimeElementDefinition[_]): StructField = {
    StructField(elementName, elementValue)
  }

  override def buildArrayValue(childDefinition: BaseRuntimeChildDefinition, elementDefinition: BaseRuntimeElementDefinition[_], elementName: String): DataType = {
    ArrayType(buildSimpleValue(childDefinition, elementDefinition, elementName))
  }

  override def buildPrimitiveDatatype(primitive: RuntimePrimitiveDatatypeDefinition): DataType = {
    dataTypeMappings.primitiveToDataType(primitive)
  }

  override def buildPrimitiveDatatypeXhtmlHl7Org(xhtmlHl7Org: RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition): DataType = DataTypes.StringType

  override def buildValue(childDefinition: BaseRuntimeChildDefinition, elementDefinition: BaseRuntimeElementDefinition[_], elementName: String): Seq[StructField] = {
    val customEncoder = dataTypeMappings.customEncoder(elementDefinition, elementName)
    customEncoder.map(_.schema2(if (isCollection(childDefinition)) Some(ArrayType(_)) else None))
      .getOrElse(super.buildValue(childDefinition, elementDefinition, elementName))
  }
}

/**
 * The converter from FHIR schemas to SQL schemas.
 *
 * @param fhirContext      the FHIR context to use.
 * @param dataTypeMappings the data type mappings to use.
 * @param config           encoder configuration to use.
 */
class SchemaConverter2(val fhirContext: FhirContext, val dataTypeMappings: DataTypeMappings, val config: EncoderConfig) extends SchemaConverter with EncoderContext {

  private[encoders2] def compositeSchema(compositeElementDefinition: BaseRuntimeElementCompositeDefinition[_ <: IBase]): DataType = {
    SchemaVisitor.traverseComposite(compositeElementDefinition, new SchemaConverterProcessor(fhirContext, dataTypeMappings, config))
  }

  override def resourceSchema(resourceDefinition: RuntimeResourceDefinition): StructType = {
    SchemaVisitor.traverseResource(resourceDefinition, new SchemaConverterProcessor(fhirContext, dataTypeMappings, config)).asInstanceOf[StructType]
  }
}