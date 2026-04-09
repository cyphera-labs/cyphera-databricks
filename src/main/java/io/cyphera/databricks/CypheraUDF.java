package io.cyphera.databricks;

import io.cyphera.Cyphera;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.api.java.UDF2;

public final class CypheraUDF {

    private CypheraUDF() {}

    /** UDF2: cyphera_protect(policyName, value) -> protectedValue */
    public static class Protect implements UDF2<String, String, String> {
        @Override
        public String call(String policyName, String value) {
            return CypheraLoader.getInstance().protect(value, policyName);
        }
    }

    /** UDF2: cyphera_access(policyName, protectedValue) -> clearValue */
    public static class AccessWithPolicy implements UDF2<String, String, String> {
        @Override
        public String call(String policyName, String protectedValue) {
            return CypheraLoader.getInstance().access(protectedValue, policyName);
        }
    }

    /** UDF1: cyphera_access(protectedValue) -> clearValue (tag-based) */
    public static class AccessByTag implements UDF1<String, String> {
        @Override
        public String call(String protectedValue) {
            return CypheraLoader.getInstance().access(protectedValue);
        }
    }
}
