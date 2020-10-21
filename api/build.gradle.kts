import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id(Plugins.springBootPlugin) version Versions.springBootVersion
    id(Plugins.springDependencyManagementPlugin) version Plugins.springDependencyManagementVersion
}

apply(plugin = Plugins.springBootPlugin)
apply(plugin = Plugins.springDependencyManagementPlugin)

allOpen {
    annotation("org.springframework.web.bind.annotation.RestController")
    annotation("org.springframework.web.bind.annotation.RequestMapping")
}

dependencies {
    implementation(project(":core"))
    implementation(Libs.Utils.fasterXmlJacksonModule)
    implementation(Libs.SpringBoot.web)
    implementation(Libs.SpringBoot.springFoxSwaggerUi)
    implementation(Libs.SpringBoot.springFoxSwagger2)
    implementation(Libs.Utils.javaxValidation)
    testImplementation(Libs.SpringBoot.Test.starterTest)
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
