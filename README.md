# test-derby

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

A small utility library for managing in-memory Apache Derby databases in tests.

## Usage

Add the dependency to your project. Because Derby is declared `provided` in this library, you must also include the Derby artifacts directly.

**Maven:**
```xml
<dependency>
    <groupId>io.github.alterioncorp</groupId>
    <artifactId>test-derby</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.apache.derby</groupId>
    <artifactId>derby</artifactId>
    <version>10.16.1.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.apache.derby</groupId>
    <artifactId>derbytools</artifactId>
    <version>10.16.1.1</version>
    <scope>test</scope>
</dependency>
```

## API

All methods are static on `io.github.alterioncorp.test.derby.DerbyEmbeddedUtils` and declare `throws SQLException`.

| Method | Description |
|---|---|
| `createDatabase(String dbName)` | Creates a new in-memory Derby database |
| `dropDatabase(String dbName)` | Drops an existing in-memory Derby database |
| `openConnection(String dbName)` | Returns an open `Connection` to the database |
| `createDataSource(String dbName)` | Returns a `DataSource` backed by the database |

**Example:**
```java
DerbyEmbeddedUtils.createDatabase("mydb");
try (Connection conn = DerbyEmbeddedUtils.openConnection("mydb")) {
    // use connection
}
DerbyEmbeddedUtils.dropDatabase("mydb");
```

## License

Licensed under the [Apache License 2.0](LICENSE).
