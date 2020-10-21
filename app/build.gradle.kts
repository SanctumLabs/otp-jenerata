import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

apply(plugin = Plugins.springBootPlugin)
apply(plugin = Plugins.springDependencyManagementPlugin)

buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(Plugins.springBootGradlePlugin)
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
}

allOpen {
    annotation("org.springframework.context.annotation.Configuration")
}

apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-spring")

dependencies {
    implementation(project(":core"))
    implementation(project(":services"))
    implementation(project(":database"))
    implementation(project(":api"))

    implementation(Libs.SpringBoot.actuator)
    implementation(Libs.SpringBoot.dataJpa)
    implementation(Libs.SpringBoot.starterWeb)

    implementation(Libs.Network.apacheHttpClient)

    annotationProcessor(Libs.Utils.lombok)

    implementation(Libs.SpringBoot.springFoxSwaggerUi)
    implementation(Libs.SpringBoot.springFoxSwagger2)

    testImplementation(Libs.SpringBoot.Test.starterTest) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.getByName<BootJar>("bootJar") {
    archiveClassifier
    enabled = true
}

tasks.getByName<BootRun>("bootRun") {
    sourceResources(sourceSets["main"])
    systemProperty(
        "spring.profiles.active",
        "dev"
    )
}
