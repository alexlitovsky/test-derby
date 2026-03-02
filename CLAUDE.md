# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`test-derby` is a Maven library (`io.github.alexlitovsky:test-derby`) that provides utility methods for managing in-memory Apache Derby databases. It is intended to be used as a test dependency by other projects.

## Build Commands

```bash
mvn clean install       # Build and install to local Maven repo
mvn clean package       # Build JAR only
mvn compile             # Compile sources
mvn javadoc:javadoc     # Generate Javadocs
```

CI uses `mvn clean verify -U` for non-main branches and `mvn clean deploy -U -Psign` for main (publishes to Maven Central).

There are no tests in this repository. The library is validated by consumers.

## Versioning

- `develop` and feature branches always use a `SNAPSHOT` version (e.g. `1.0-SNAPSHOT`)
- The version is bumped to a release version (e.g. `1.0.0`) before merging to `main`

## Architecture

The entire library is one class: `DerbyEmbeddedUtils` (`src/main/java/com/alexlitovsky/test/derby/DerbyEmbeddedUtils.java`).

- All methods are static; the class is not instantiated.
- Uses the Derby embedded memory protocol: `jdbc:derby:memory:{dbName}`
- `EmbeddedDriver` is loaded via a static initializer to ensure Derby registers itself before any `DriverManager` calls.
- All public methods declare `throws SQLException`; callers are responsible for handling SQL errors.
- `dropDatabase` intentionally swallows `SQLNonTransientConnectionException` — Derby always throws this on a successful drop.
- Derby dependencies (`derby`, `derbytools`) are declared as `provided` scope, meaning consumers must include them on their classpath.

## Java & Maven

- Java 21 (source and target)
- Inherits from `io.github.alexlitovsky:parent-pom:1.0.0` for shared plugin/repository configuration
- Source and Javadoc JARs are attached at build time via `maven-source-plugin` and `maven-javadoc-plugin`
- Artifacts are published to Maven Central via `central-publishing-maven-plugin` (inherited from parent-pom)
- GPG signing is activated via the `sign` profile (`-Psign`), managed by parent-pom
