buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(Plugins.kotlinGradlePluginNoargs)
        classpath(Plugins.springBootGradlePlugin)
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
    id(Plugins.springBootPlugin) version Versions.springBootVersion
    id(Plugins.springDependencyManagementPlugin) version Plugins.springDependencyManagementVersion
}

allOpen {
    annotation("org.springframework.context.annotation.Configuration")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("org.springframework.stereotype.Repository")
    annotation("org.springframework.stereotype.Component")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":database"))
    implementation(project(":services"))

    implementation(Libs.SpringBoot.web)
    implementation(Libs.SpringBoot.starter)
    implementation(Libs.SpringBoot.starterJdbc)
    implementation(Libs.SpringBoot.dataJpa)
    implementation(Libs.SpringBoot.batch)
    implementation(Libs.Utils.thymeleaf)
    implementation(Libs.Data.postgres)
    implementation(Libs.Data.h2Database)
    implementation(Libs.Data.mysqlDbJdbc)

    testImplementation(Libs.SpringBoot.Test.starterTest) {
        exclude(module = "junit")
    }
}
