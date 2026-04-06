package dev.cyphera.databricks;

import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.api.java.UDF3;

public final class CypheraUDF {

    private CypheraUDF() {}

    // Policy-based API
    public static class Protect implements UDF2<String, String, String> {
        @Override
        public String call(String policyName, String value) {
            PolicyEntry policy = PolicyLoader.getInstance().getPolicy(policyName);
            if (policy == null) return "[unknown policy: " + policyName + "]";
            return DummyCipher.encrypt(value, policy.alphabet(), policy.keyMaterial());
        }
    }

    public static class Unprotect implements UDF2<String, String, String> {
        @Override
        public String call(String policyName, String value) {
            PolicyEntry policy = PolicyLoader.getInstance().getPolicy(policyName);
            if (policy == null) return "[unknown policy: " + policyName + "]";
            return DummyCipher.decrypt(value, policy.alphabet(), policy.keyMaterial());
        }
    }

    // Direct engine API
    public static class FF1Encrypt implements UDF3<String, String, String, String> {
        @Override
        public String call(String value, String keyHex, String alphabet) {
            return DummyCipher.encrypt(value, alphabet, keyHex);
        }
    }

    public static class FF1Decrypt implements UDF3<String, String, String, String> {
        @Override
        public String call(String value, String keyHex, String alphabet) {
            return DummyCipher.decrypt(value, alphabet, keyHex);
        }
    }
}
