import org.springframework.boot.gradle.tasks.bundling.BootJar

apply(plugin = Plugins.springBootPlugin)
apply(plugin = Plugins.springDependencyManagementPlugin)

plugins {
    kotlin("jvm")
    id(Plugins.openApiGeneratorPlugin) version Plugins.openApiGeneratorPluginVersion
    id(Plugins.springBootPlugin) version Versions.springBootVersion
    id(Plugins.springDependencyManagementPlugin) version Plugins.springDependencyManagementVersion
}

val emailClientContract = "$projectDir/src/main/resources/contracts/email-gateway.yaml"
val emailClientPackageName = "com.garihub.user.services.email"
val emailClientOutputDir = "$buildDir/email-client"

// Ref: https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-gradle-plugin/README.adoc
val validateEmailClientSpec by tasks.registering(org.openapitools.generator.gradle.plugin.tasks.ValidateTask::class) {
    group = "codegen"
    description = "Validates Email Client OpenApi Spec"
    inputSpec.set(emailClientContract)
    recommend.set(true)
}

val generateEmailClient by tasks.registering(org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
    group = "codegen"
    description = "Generates EmailGateway OpenApi Spec"
    id.set("email-gateway-client")
    generatorName.set("kotlin")
    inputSpec.set(emailClientContract)
    outputDir.set(emailClientOutputDir)
    apiPackage.set("$emailClientPackageName.api")
    modelPackage.set("$emailClientPackageName.dto")
    modelNameSuffix.set("Dto")
    library.set("jvm-retrofit2")
    mustRunAfter(validateEmailClientSpec, "build")
    doLast {
        logger.info(">> Done generating spec")
    }
}

dependencies {
    implementation(project(":core"))
    implementation(Libs.SpringBoot.web)
    implementation(Libs.Utils.fasterXmlJacksonModule)
    implementation(Libs.SpringBoot.thymeleaf)
    compileOnly(Libs.Utils.lombok)
    testImplementation(Libs.SpringBoot.Test.starterTest)
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
