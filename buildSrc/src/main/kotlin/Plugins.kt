object Plugins {
    const val springBootPlugin = "org.springframework.boot"
    const val springDependencyManagementVersion = "1.0.10.RELEASE"
    const val springDependencyManagementPlugin = "io.spring.dependency-management"

    const val springBootGradlePlugin =
        "org.springframework.boot:spring-boot-gradle-plugin:${Versions.springBootVersion}"

    const val javaPlugin = "java"

    const val openApiGeneratorPluginVersion = "4.3.1"
    const val openApiGeneratorPlugin = "org.openapi.generator"
    const val openApiToolsGeneratorGradlePlugin = "org.openapitools:openapi-generator-gradle-plugin"

    const val jacocoVersion = "0.8.5"
    const val jacocoPlugin = "org.jacoco:org.jacoco.core:$jacocoVersion"

    const val dokkaPluginVersion = "0.10.1"
    const val dokkaGradlePlugin = "org.jetbrains.dokka:dokka-gradle-plugin"
    const val dokkaPlugin = "org.jetbrains.dokka"

    const val detektPluginVersion = "1.9.1"
    const val detektPlugin = "io.gitlab.arturbosch.detekt"

    const val gradleBuildPluginVersion = "3.3.1"
    const val kotlinGradlePluginVersion = "1.3.70"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val kotlinGradlePluginAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlinVersion}"
    const val kotlinGradlePluginNoargs = "org.jetbrains.kotlin:kotlin-noarg:${Versions.kotlinVersion}"

    // db migrations
    const val flywayPluginVersion = "6.5.0"
    const val flywayDbPlugin = "org.flywaydb.flyway"

    // docker builds
    const val palantirDockerPluginVersion = "0.25.0"
    const val palantirDockerPlugin = "com.palantir.docker"

    const val jibPlugin = "com.google.cloud.tools.jib"
    const val jibPluginVersion = "2.4.0"

    const val sonarQubePluginVersion = "3.0"
    const val sonarQubePlugin = "org.sonarqube"
}
