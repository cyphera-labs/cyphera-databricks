# cyphera-databricks

[![CI](https://github.com/cyphera-labs/cyphera-databricks/actions/workflows/ci.yml/badge.svg)](https://github.com/cyphera-labs/cyphera-databricks/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](LICENSE)

Format-preserving encryption for [Databricks](https://www.databricks.com/) — Spark UDF powered by Cyphera.

Built on [`io.cyphera:cyphera`](https://central.sonatype.com/artifact/io.cyphera/cyphera) from Maven Central.

> This integration requires a Databricks workspace. See below for deployment instructions.

## Build

### From source

```bash
mvn package -DskipTests
```

Produces `target/cyphera-databricks-0.1.0.jar` (fat JAR with all dependencies).

### Via Docker

```bash
docker build -t cyphera-databricks .
```

## Install / Deploy

### Option 1: Cluster Library

Upload `target/cyphera-databricks-0.1.0.jar` as a cluster library in the Databricks workspace.

### Option 2: Unity Catalog Volume

```sql
PUT 'target/cyphera-databricks-0.1.0.jar' INTO '/Volumes/catalog/schema/jars/';
```

### Register UDFs

In a Databricks notebook:

```python
spark._jvm.io.cyphera.databricks.CypheraRegistrar.registerAll(spark._jsparkSession)
```

Or in Scala:
```scala
io.cyphera.databricks.CypheraRegistrar.registerAll(spark)
```

### Policy Configuration

Place `cyphera.json` at `/etc/cyphera/cyphera.json` on the cluster, or set the `CYPHERA_POLICY_FILE` environment variable in the cluster configuration.

## Usage

```sql
-- Protect with a named policy
SELECT cyphera_protect('ssn', '123-45-6789');
-- → 'T01i6J-xF-07pX' (tagged, dashes preserved)

-- Access — tag tells Cyphera which policy to use
SELECT cyphera_access_tag(cyphera_protect('ssn', '123-45-6789'));
-- → '123-45-6789'

-- Bulk protect
SELECT name, cyphera_protect('ssn', ssn) AS protected_ssn
FROM customers;
```

## Operations

### Policy Configuration

- Policy file: `/etc/cyphera/cyphera.json` or `CYPHERA_POLICY_FILE` env var
- Set env var in Databricks cluster Spark configuration
- Policy loaded on first UDF call — restart cluster to reload

### Monitoring

- UDF errors surface as Spark task failures
- Check cluster driver logs for `CypheraLoader` entries

### Upgrading

1. Build a new JAR with the updated SDK version
2. Replace the cluster library or volume JAR
3. Restart the cluster

### Troubleshooting

- **UDF not found** — `CypheraRegistrar.registerAll(spark)` not called, or JAR not attached to cluster
- **"Unknown policy"** — check that cyphera.json is accessible from the cluster
- **ClassNotFoundException** — JAR not on the classpath, re-upload and restart

## Policy File

```json
{
  "policies": {
    "ssn": { "engine": "ff1", "key_ref": "demo-key", "tag": "T01" },
    "credit_card": { "engine": "ff1", "key_ref": "demo-key", "tag": "T02" }
  },
  "keys": {
    "demo-key": { "material": "2B7E151628AED2A6ABF7158809CF4F3C" }
  }
}
```

## Future

- Unity Catalog function registration (SQL-based, no notebook needed)
- Delta Lake integration (encrypt on write, access on read)
- Init script for automatic UDF registration on cluster start

## License

Apache 2.0 — Copyright 2026 Horizon Digital Engineering LLC
