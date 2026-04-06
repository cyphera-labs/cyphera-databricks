-- Cyphera Databricks UDF Demo
-- Prerequisites:
--   1. Upload cyphera-databricks-0.1.0.jar to a Unity Catalog volume or cluster library
--   2. Register UDFs (via notebook or init script):
--      spark.udf().register("cyphera_protect", new dev.cyphera.databricks.CypheraUDF.Protect(), StringType)
--
-- Or use the registrar in a notebook cell:
--   %scala
--   dev.cyphera.databricks.CypheraRegistrar.registerAll(spark)

-- Policy-based encryption
SELECT cyphera_protect('ssn', '123-45-6789') AS encrypted_ssn;
SELECT cyphera_unprotect('ssn', cyphera_protect('ssn', '123-45-6789')) AS decrypted_ssn;

-- Round-trip proof
SELECT
    '123-45-6789' AS original,
    cyphera_protect('ssn', '123-45-6789') AS encrypted,
    cyphera_unprotect('ssn', cyphera_protect('ssn', '123-45-6789')) AS decrypted;

-- Direct engine API
SELECT cyphera_ff1_encrypt('123456789', '2B7E151628AED2A6ABF7158809CF4F3C', 'digits') AS encrypted;

-- Bulk example
SELECT
    name,
    ssn AS original_ssn,
    cyphera_protect('ssn', ssn) AS protected_ssn
FROM (
    SELECT 'Alice' AS name, '123-45-6789' AS ssn
    UNION ALL SELECT 'Bob', '987-65-4321'
    UNION ALL SELECT 'Carol', '555-12-3456'
);
