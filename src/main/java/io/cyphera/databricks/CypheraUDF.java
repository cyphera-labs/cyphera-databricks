package io.cyphera.databricks;

import io.cyphera.Cyphera;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.api.java.UDF2;

public final class CypheraUDF {

    private CypheraUDF() {}

    /** UDF2: cyphera_protect(configurationName, value) -&gt; protectedValue */
    public static class Protect implements UDF2<String, String, String> {
        @Override
        public String call(String configurationName, String value) {
            return CypheraLoader.getInstance().protect(value, configurationName);
        }
    }

    /** UDF2: cyphera_access_with_configuration(configurationName, protectedValue) -&gt; clearValue (escape hatch for headerless configurations) */
    public static class AccessWithConfiguration implements UDF2<String, String, String> {
        @Override
        public String call(String configurationName, String protectedValue) {
            return CypheraLoader.getInstance().access(protectedValue, configurationName);
        }
    }

    /** UDF1: cyphera_access(protectedValue) -&gt; clearValue (header-driven, primary) */
    public static class Access implements UDF1<String, String> {
        @Override
        public String call(String protectedValue) {
            return CypheraLoader.getInstance().access(protectedValue);
        }
    }
}
