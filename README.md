# cyphera-databricks

Cyphera format-preserving encryption UDFs for Databricks and Apache Spark.

## Build

```bash
mvn package -DskipTests
```

## Deploy

### Option 1: Cluster Library
Upload `target/cyphera-databricks-0.1.0.jar` as a cluster library in the Databricks workspace.

### Option 2: Unity Catalog Volume
```sql
PUT 'target/cyphera-databricks-0.1.0.jar' INTO '/Volumes/catalog/schema/jars/';
```

## Register UDFs

```python
# In a Databricks notebook
spark._jvm.io.cyphera.databricks.CypheraRegistrar.registerAll(spark._jsparkSession)
```

Or in Scala:
```scala
io.cyphera.databricks.CypheraRegistrar.registerAll(spark)
```

## Usage

```sql
SELECT cyphera_protect('ssn', '123-45-6789');
-- Returns format-preserved encrypted SSN like '890-12-3456'

SELECT cyphera_unprotect('ssn', cyphera_protect('ssn', '123-45-6789'));
-- Returns '123-45-6789'
```
