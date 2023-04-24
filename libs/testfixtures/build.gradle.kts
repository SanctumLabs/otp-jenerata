plugins {
    java
    `java-test-fixtures`
    kotlin("jvm")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testFixturesImplementation(Dependencies.Test.TestContainers.postgresql)
    testFixturesImplementation(Dependencies.Test.TestContainers.junitJupiter)
    testFixturesImplementation(Dependencies.Test.Jupiter.api)
    testFixturesImplementation(Dependencies.Test.Jupiter.engine)
}
