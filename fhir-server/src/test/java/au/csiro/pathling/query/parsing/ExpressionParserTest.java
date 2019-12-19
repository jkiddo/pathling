/*
 * Copyright © Australian e-Health Research Centre, CSIRO. All rights reserved.
 */

package au.csiro.pathling.query.parsing;

import static au.csiro.pathling.query.parsing.PatientListBuilder.PATIENT_ID_8ee183e2;
import static au.csiro.pathling.query.parsing.PatientListBuilder.PATIENT_ID_9360820c;
import static au.csiro.pathling.query.parsing.PatientListBuilder.allPatientsNull;
import static au.csiro.pathling.query.parsing.PatientListBuilder.allPatientsWithValue;
import static au.csiro.pathling.test.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.hl7.fhir.r4.model.Enumerations.ResourceType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import au.csiro.pathling.TestUtilities;
import au.csiro.pathling.fhir.FreshFhirContextFactory;
import au.csiro.pathling.fhir.TerminologyClient;
import au.csiro.pathling.query.ResourceReader;
import au.csiro.pathling.test.ParsedExpressionAssert;

/**
 * @author Piotr Szul
 */
@Category(au.csiro.pathling.UnitTest.class)
public class ExpressionParserTest {
	
	private SparkSession spark;
	private ResourceReader mockReader;
	private TerminologyClient terminologyClient;
	private String terminologyServiceUrl = "https://r4.ontoserver.csiro.au/fhir";
	private ExpressionParserContext parserContext;
	private ExpressionParser expressionParser;

	@Before
	public void setUp() throws IOException {
		spark = SparkSession.builder().appName("pathling-test").config("spark.master", "local")
		    .config("spark.driver.host", "localhost").config("spark.sql.shuffle.partitions", "1").getOrCreate();

		terminologyClient = mock(TerminologyClient.class, Mockito.withSettings().serializable());
		when(terminologyClient.getServerBase()).thenReturn(terminologyServiceUrl);

		mockReader = mock(ResourceReader.class);

		// Gather dependencies for the execution of the expression parser.
		parserContext = new ExpressionParserContext();
		parserContext.setFhirContext(TestUtilities.getFhirContext());
		parserContext.setFhirContextFactory(new FreshFhirContextFactory());
		parserContext.setTerminologyClient(terminologyClient);
		parserContext.setSparkSession(spark);
		parserContext.setResourceReader(mockReader);
		parserContext.setSubjectContext(null);
		expressionParser = new ExpressionParser(parserContext);
		mockResourceReader(ResourceType.PATIENT);

		ResourceType resourceType = ResourceType.PATIENT;
		Dataset<Row> subject = mockReader.read(resourceType);
		String firstColumn = subject.columns()[0];
		String[] remainingColumns = Arrays.copyOfRange(subject.columns(), 1, subject.columns().length);
		Column idColumn = subject.col("id");
		subject = subject.withColumn("resource", org.apache.spark.sql.functions.struct(firstColumn, remainingColumns));
		Column valueColumn = subject.col("resource");
		subject = subject.select(idColumn, valueColumn);

		// Build up an input for the function.
		ParsedExpression subjectResource = new ParsedExpression();
		subjectResource.setFhirPath("%resource");
		subjectResource.setResource(true);
		subjectResource.setResourceType(ResourceType.PATIENT);
		subjectResource.setOrigin(subjectResource);
		subjectResource.setDataset(subject);
		subjectResource.setIdColumn(idColumn);
		subjectResource.setSingular(true);
		subjectResource.setValueColumn(valueColumn);

		parserContext.setSubjectContext(subjectResource);	
	}


	private void mockResourceReader(ResourceType... resourceTypes) {
		for (ResourceType resourceType : resourceTypes) {
			URL parquetUrl = Thread.currentThread().getContextClassLoader()
			    .getResource("test-data/parquet/" + resourceType + ".parquet");
			assertThat(parquetUrl).isNotNull();
			Dataset<Row> dataset = spark.read().parquet(parquetUrl.toString());
			when(mockReader.read(resourceType)).thenReturn(dataset);
			when(mockReader.getAvailableResourceTypes()).thenReturn(new HashSet<>(Arrays.asList(resourceTypes)));
		}
	}
	
	private ParsedExpressionAssert assertThatResultOf(String expression) {
		return assertThat(expressionParser.parse(expression));
	}
	
	@Test
	public void testContainsOperator() {
		assertThatResultOf("name.family contains 'Wuckert783'")
			.isOfBooleanType()
		  .isSelection()
		  .selectResult()
		  .hasRows(allPatientsWithValue(false).withRow(PATIENT_ID_9360820c, true));
		
		assertThatResultOf("name.suffix contains 'MD'")
		.isOfBooleanType()
	  .isSelection()
	  .selectResult()
	  .hasRows(allPatientsNull().withRow(PATIENT_ID_8ee183e2, true));
  }
	
	
	@Test
	public void testInOperator() {
		assertThatResultOf("'Wuckert783' in name.family")
			.isOfBooleanType()
		  .isSelection()
		  .selectResult()
		  .hasRows(allPatientsWithValue(false).withRow(PATIENT_ID_9360820c, true));
		
		assertThatResultOf("'MD' in name.suffix")
		.isOfBooleanType()
	  .isSelection()
	  .selectResult()
	  .hasRows(allPatientsNull().withRow(PATIENT_ID_8ee183e2, true));
	}
	
	@After
	public void tearDown() {
		spark.close();
	}
}
