package au.csiro.pathling.sql.udf;

import au.csiro.pathling.terminology.TerminologyService2;
import au.csiro.pathling.terminology.TerminologyServiceFactory;
import au.csiro.pathling.terminology.TerminologyService;
import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;

@Slf4j
abstract public class ValidateCodingBase implements SqlFunction, Serializable {

  private static final long serialVersionUID = 7605853352299165569L;

  @Nonnull
  protected final TerminologyServiceFactory terminologyServiceFactory;

  protected ValidateCodingBase(@Nonnull final TerminologyServiceFactory terminologyServiceFactory) {
    this.terminologyServiceFactory = terminologyServiceFactory;
  }

  @Override
  public DataType getReturnType() {
    return DataTypes.BooleanType;
  }

  @Nullable
  protected Boolean doCall(@Nullable final Stream<Coding> codings, @Nullable final String url) {
    if (url == null || codings == null) {
      return null;
    }
    final TerminologyService2 terminologyService = terminologyServiceFactory.buildService2();
    return codings
        .filter(Objects::nonNull).anyMatch(coding -> {
          final Parameters parameters = terminologyService.validate(url, coding);
          return TerminologyUdfHelpers.isTrue(parameters);
        });
  }

}
