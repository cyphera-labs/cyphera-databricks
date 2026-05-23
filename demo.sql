-- Cyphera Databricks UDF Demo
-- Prerequisites:
--   1. Upload cyphera-databricks-0.1.0.jar to a Unity Catalog volume or cluster library
--   2. Register UDFs via notebook:
--      %scala
--      io.cyphera.databricks.CypheraRegistrar.registerAll(spark)

-- Protect with a named configuration (output is header-prefixed)
SELECT cyphera_protect('ssn', '123-45-6789') AS protected_ssn;

-- Access — the embedded header tells Cyphera which configuration to use; no name needed
SELECT cyphera_access(cyphera_protect('ssn', '123-45-6789')) AS accessed_ssn;

-- Access with explicit configuration name (escape hatch for headerless configurations)
SELECT cyphera_access_with_configuration('ssn', cyphera_protect('ssn', '123-45-6789')) AS accessed_ssn;

-- Round-trip proof
SELECT
    '123-45-6789' AS original,
    cyphera_protect('ssn', '123-45-6789') AS protected,
    cyphera_access(cyphera_protect('ssn', '123-45-6789')) AS accessed;

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
