# OTP Jenereta

[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg)](https://conventionalcommits.org)

Backend API service for One Time Password generation

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing 
purposes.

### Pre-requisites

The preferred IDE of choice is [IntelliJ](https://www.jetbrains.com/idea/), although any other editor or IDE can be used for development.

1. [Gradle](https://gradle.org/)

This is the build tool that is used to handle dependencies and configurations. You can use the [gradle wrapper](./gradlew) (for UNIX based systems) or [gradlew bat](./gradlew.bat)
for Windows based systems to handle dependency management and tasks. This wrapper will handle using the correct version 
for the project. However, gradle itself can still be downloaded and used(just use version 4.9+ :D )

2. [Java](https://www.oracle.com/java/), [Kotlin](https://kotlinlang.org)

Kotlin is the preferred language used to build this service due to its interoperability with Java as well as the added features
it comes bundled with. However, since it compiles down to Java which will then run on JVM, you need to have JAVA_HOME set up on
your local development machine. Install Java onto your local development environment. You can easily install Java and Kotlin 
with a nifty tool [sdkman](https://sdkman.io/) which will handle all that for you.

3. [Docker](https://www.docker.com/)

Docker will handle building an image for this service which will be used in the production environment to build containers. This allows
us to create a deployable and easily configurable application that can run on any environment(well, almost).

### Installing

Getting up and running should be a simple step. Ensure you have the following setup:

1. __Dependencies installed using gradle__:

    This can be done as follows:
    
    ```bash
    ./gradlew check
    ```
    
    > This is a verification task, however, if this is the first time running any gradle task, the dependencies will be downloaded and
    > and set in the gradle home directory for caching(this will be located in the .gradle directory of you user home)


These should get you setup and up and running.

## Running tests

Tests have been configured to run with [JUnit 5](https://junit.org/junit5/). Run tests with the following command:

```bash
./gradlew test
```

> This will run all unit tests under [test](app/src/test/kotlin/)

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

After the db instance is up, you will need  to run the database migrations:

```
$ ./gradlew :migrations:flywayMigrate
```

Then you can run the application using the `bootRun` task provided by the Spring plugin.

```bash
$ ./gradlew :app:bootRun
```

The jdk version used is `11.0`.

Valid arguments you can pass in are:

1. spring.datasource.url=$SPRING_DATASOURCE_URL
2. spring.datasource.username=$SPRING_DATASOURCE_USERNAME
3. spring.datasource.password=$SPRING_DATASOURCE_PASSWORD
4. spring.datasource.driver-class-name=$SPRING_DATASOURCE_DRIVER_CLASS_NAME

> Where the $VARIABLE_NAME is the name to pass in as an argument

## SonarQube Integration

To check quality, maintainability & vulnerability of the code, [SonarQube](https://docs.sonarqube.org/latest/) has been
used to scan. This is done with the help of [SonarQube's Gradle Plugin](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-gradle/).
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
3. [Spring Boot](https://spring.io) - Spring framework 
4. [Gradle](https://gradle.org) - Build and dependency management tool
5. [JUnit 5](https://junit.org/junit5/) - Test framework 
6. [JaCoCo](http://www.eclemma.org/jacoco/) - Test coverage reporter
