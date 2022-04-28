from pyspark.sql import DataFrame


def _context(_jvm):
    return _jvm.au.csiro.pathling.api.PathlingContext


EQ_EQUIVALENT = "equivalent"


class PathlingContext:
    def __init__(self, sparkSession, serverUrl):
        self._sparkSession = sparkSession
        self._jctx = _context(sparkSession._jvm).create(serverUrl)

        sparkSession._jvm.au.csiro.pathling.sql.PathlingStrategy.setup(sparkSession._jsparkSession)

    def memberOf(self, df, codingColumn, valueSetUrl, outputColumnName):
        return DataFrame(
            self._jctx.memberOf(df._jdf, codingColumn._jc, valueSetUrl, outputColumnName),
            self._sparkSession._wrapped)

    def translate(self, df, codingColumn, conceptMapUri, reverse=False, equivalence=EQ_EQUIVALENT,
                  outputColumnName="result"):
        return DataFrame(
            self._jctx.translate(df._jdf, codingColumn._jc, conceptMapUri, reverse, equivalence,
                                 outputColumnName), self._sparkSession._wrapped)
