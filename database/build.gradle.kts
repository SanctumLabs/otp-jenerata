import org.springframework.boot.gradle.tasks.bundling.BootJar

apply(plugin = Plugins.springBootPlugin)
apply(plugin = Plugins.springDependencyManagementPlugin)

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
    id(Plugins.springBootPlugin) version Versions.springBootVersion
    id(Plugins.springDependencyManagementPlugin) version Plugins.springDependencyManagementVersion
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("org.springframework.stereotype.Repository")
    annotation("org.springframework.stereotype.Component")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation(project(":core"))
    implementation(Libs.Data.springBootJdbc)
    implementation(Libs.SpringBoot.dataJpa)
    implementation(Libs.Data.postgres)
    implementation(Libs.Data.h2Database)
    implementation(Libs.Data.mysqlDbJdbc)
    compileOnly(Libs.Utils.lombok)
    testImplementation(Libs.SpringBoot.Test.starterTest)
}
