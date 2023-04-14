object Plugins {
    object Ktor {
        const val version = "2.2.4"
        const val plugin = "io.ktor.plugin"
    }

    const val KotlinSerialization = "org.jetbrains.kotlin.plugin.serialization"

    const val Java = "java"

    object Jacoco {
        const val version = "0.8.5"
        const val plugin = "jacoco"
        const val core = "org.jacoco:org.jacoco.core:$version"
    }

    object Dokka {
        const val version = "0.10.1"
        const val gradlePlugin = "org.jetbrains.dokka:dokka-gradle-plugin"
        const val plugin = "org.jetbrains.dokka"
    }

    object Detekt {
        const val version = "1.9.1"
        const val plugin = "io.gitlab.arturbosch.detekt"
    }

    const val gradleBuildPluginVersion = "3.3.1"
    const val kotlinGradlePluginVersion = "1.3.70"

    const val KotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KotlinVersion}"

    // db migrations
    const val flywayPluginVersion = "6.5.0"
    const val flywayDbPlugin = "org.flywaydb.flyway"

    const val sonarQubePluginVersion = "3.0"
    const val sonarQubePlugin = "org.sonarqube"
}
