package dev.cyphera.databricks;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

public final class CypheraRegistrar {

    private CypheraRegistrar() {}

    public static void registerAll(SparkSession spark) {
        spark.udf().register("cyphera_protect", new CypheraUDF.Protect(), DataTypes.StringType);
        spark.udf().register("cyphera_unprotect", new CypheraUDF.Unprotect(), DataTypes.StringType);
        spark.udf().register("cyphera_ff1_encrypt", new CypheraUDF.FF1Encrypt(), DataTypes.StringType);
        spark.udf().register("cyphera_ff1_decrypt", new CypheraUDF.FF1Decrypt(), DataTypes.StringType);
    }
}
