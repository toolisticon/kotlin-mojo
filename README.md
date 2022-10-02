# kotlin-mojo-helper

[![incubating](https://img.shields.io/badge/lifecycle-INCUBATING-orange.svg)](https://github.com/holisticon#open-source-lifecycle)

A library that makes writing powerful maven plugins even easier by providing kotlin extensions and convenience functions for common use cases.

[![Build Status](https://github.com/toolisticon/kotlin-mojo-helper/workflows/Development%20branches/badge.svg)](https://github.com/toolisticon/kotlin-mojo-helper/actions)
[![sponsored](https://img.shields.io/badge/sponsoredBy-Holisticon-RED.svg)](https://holisticon.de/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.maven/kotlin-mojo-helper/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.maven/kotlin-mojo-helper)


## How to use

In your maven command project (maven packaging `maven-command`) include this library:

```xml
<dependency>
  <groupId>io.toolisticon.maven</groupId>
  <artifactId>kotlin-mojo-helper</artifactId>
  <version>LATEST_VERSION</version>
</dependency>
```

The current latest version published on maven central can be found via the `maven central` badge above.

## Build

Run `mvn package` before editing in IDE, so the required maven.properties file is generated.
