# OTP Jenereta

[![Build](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/build.yml/badge.svg)](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/build.yml)
[![Lint](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/lint.yml/badge.svg)](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/lint.yml)
[![Release](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/release.yml/badge.svg)](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/release.yml)
[![Test](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/test.yml/badge.svg)](https://github.com/SanctumLabs/otp-jenerata/actions/workflows/test.yml)
[![License](https://img.shields.io/github/license/sanctumlabs/otp-jenerata)](https://github.com/sanctumlabs/otp-jenerata/blob/main/.github/LICENSE)
[![Version](https://img.shields.io/github/v/release/SanctumLabs/otp-jenereta?color=%235351FB&label=version)](https://github.com/SanctumLabs/otp-jenerata/releases)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/084c8c187fb3410bbf8987e3e99e5922)](https://app.codacy.com/gh/SanctumLabs/otp-jenerata/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.8.21-blue.svg)](https://kotlinlang.org)

Backend API service for One Time Password generation.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes.

### Pre-requisites

The preferred IDE of choice is [IntelliJ](https://www.jetbrains.com/idea/), although any other editor or IDE can be used
for development.

1. [Gradle](https://gradle.org/)

This is the build tool that is used to handle dependencies and configurations. You can use
the [gradle wrapper](./gradlew) (for UNIX based systems) or [gradlew bat](./gradlew.bat)
for Windows based systems to handle dependency management and tasks. This wrapper will handle using the correct version
for the project. However, gradle itself can still be downloaded and used(just use version 4.9+ :D )

2. [Java](https://www.oracle.com/java/), [Kotlin](https://kotlinlang.org)

Kotlin is the preferred language used to build this service due to its interoperability with Java as well as the added
features
it comes bundled with. However, since it compiles down to Java which will then run on JVM, you need to have JAVA_HOME
set up on
your local development machine. Install Java onto your local development environment. You can easily install Java and
Kotlin
with a nifty tool [sdkman](https://sdkman.io/) which will handle all that for you.

3. [Docker](https://www.docker.com/)

Docker will handle building an image for this service which will be used in the production environment to build
containers. This allows
us to create a deployable and easily configurable application that can run on any environment(well, almost).

### Installing

Getting up and running should be a simple step. Ensure you have the following setup:

1. __Dependencies installed using gradle__:

   This can be done as follows:

    ```bash
    ./gradlew check
    ```

   > This is a verification task, however, if this is the first time running any gradle task, the dependencies will be
   downloaded and
   > and set in the gradle home directory for caching(this will be located in the .gradle directory of you user home)

These should get you setup and up and running.

## Running tests

There are 3 types of tests that can be run; `unit tests`, `integration tests` & `end to end tests`. These can be run
like below:

```shell
./gradlew unittests
```

> Will run unit tests

```shell
./gradlew integrationtests
```

> Will run integration tests, ensure that you have Docker installed, as this will require docker to run.


Lastly, end to end tests. In order to run this kind of test, the database has to be running. This can be run using
Docker or using a local installation of the database. This example uses docker:

```shell
docker compose up -d
./gradlew e2e
```

> This will run End to end tests. Or the `docker compose up` command can be run in a separate terminal session.

All tests can be run however, using the below command:

```bash
docker compose up -d
./gradlew test
```

Run a test coverage report with:

```bash
./gradlew jacocoTestReport
```

> Generates a coverage report with [JaCoCo](http://www.eclemma.org/jacoco/)

## Running the application

First you need to have a running mysql instance. This can be done using `docker-compose` command

```bash
$ docker-compose up
```

Then you can run the application using the `bootRun` task provided by the Spring plugin.

```bash
$ ./gradlew :app:run
```

The jdk version used is `11.0`.

## SonarQube Integration

To check quality, maintainability & vulnerability of the code, [SonarQube](https://docs.sonarqube.org/latest/) has been
used to scan. This is done with the help
of [SonarQube's Gradle Plugin](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-gradle/).
To test this locally, there is a [docker-compose file](./docker-compose-sonar.yml) that can start a local installation
of SonarQube in a Docker container.

Start this with:

```bash
docker-compose -f docker-compose-sonar.yml up
```

> Note, ensure you have docker-compose already installed

Afterwards in the root of the project run:

```bash
./gradlew sonarqube
```

This will then generate the report on SonarQube which can be accessed [here](http://localhost:9000/)(assuming you have a
running docker container of SonarQube.

Local credentials are:

```
username: admin
password: admin
```

## Architecture

Detailed instructions on architecture can be found [here](./docs/Architecture.md)

## Built With

1. [Kotlin](https://kotlinlang.org) - Source language
2. [Detekt](https://ktlint.github.io/) - Kotlin static code analysis
3. [Ktor](https://ktor.io/) - Ktor framework
4. [Gradle](https://gradle.org) - Build and dependency management tool
5. [JUnit 5](https://junit.org/junit5/) - Test framework
6. [JaCoCo](http://www.eclemma.org/jacoco/) - Test coverage reporter
