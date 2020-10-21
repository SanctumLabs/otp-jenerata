import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

apply(plugin = Plugins.springBootPlugin)
apply(plugin = Plugins.springDependencyManagementPlugin)

plugins {
    kotlin("jvm")
    id(Plugins.palantirDockerPlugin)
    id(Plugins.jibPlugin) version Plugins.jibPluginVersion
    id(Plugins.flywayDbPlugin) version Plugins.flywayPluginVersion
    id(Plugins.springBootPlugin) version Versions.springBootVersion
    id(Plugins.springDependencyManagementPlugin) version Plugins.springDependencyManagementVersion
}

// REF: https://flywaydb.org/documentation/gradle/
flyway {
    url = System.getenv("SPRING_DATASOURCE_URL") ?: "jdbc:postgresql://localhost:5434/users"
    user = System.getenv("SPRING_DATASOURCE_USERNAME") ?: "user"
    password = System.getenv("SPRING_DATASOURCE_PASSWORD") ?: "user-pass"
    schemas = arrayOf(System.getenv("DB_SCHEMA") ?: "public")
}

dependencies {
    implementation(Libs.SpringBoot.starter)
    implementation(Libs.SpringBoot.starterJdbc)
    implementation(Libs.SpringBoot.dataJpa)
    implementation(Libs.Data.mysqlDbJdbc)
    implementation(Libs.Data.postgres)
    implementation(Libs.Data.flywayCore)
}

tasks.getByName<BootJar>("bootJar") {
    archiveClassifier
}

tasks.getByName<BootRun>("bootRun") {
    sourceResources(sourceSets["main"])
    systemProperty(
        "spring.profiles.active",
        "dev"
    )
}

jib {
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
        labels = mapOf(Pair("MAINTAINER", "Ceberus"))
    }
    to {
        image =
            "${System.getenv("BUILD_ENV_DOCKER_REGISTRY") ?: "garihublimited"}/user-service-migrations:1.0-SNAPSHOT"
    }
}

// ref https://github.com/palantir/gradle-docker
docker {
    name = "${System.getenv("BUILD_ENV_DOCKER_REGISTRY") ?: "garihublimited"}/user-service-migrations:1.0-SNAPSHOT"
    setDockerfile(file("../Dockerfile.migrations"))
    copySpec.from("build/libs/migrations-0.0.1-SNAPSHOT.jar").into("build")
    buildArgs(mapOf("JAR_FILE" to "build/migrations-0.0.1-SNAPSHOT.jar"))
}
