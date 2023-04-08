import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.KotlinVersion
    id(Plugins.KtorPlugin) version Plugins.KtorPluginVersion
    id(Plugins.KotlinSerialization) version Versions.KotlinVersion
}

group = MetaInfo.GROUP

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "${JavaVersion.VERSION_11}"
            }
        }
    }

    dependencies {

    }
}

dependencies {
    implementation(Dependencies.Ktor.coreJvm)
    implementation(Dependencies.Ktor.openApi)
    implementation(Dependencies.Ktor.serializationJson)
    implementation(Dependencies.Ktor.contentNegotiation)
    implementation(Dependencies.Ktor.metrics)
    implementation(Dependencies.Ktor.metricsMicrometer)
    implementation(Dependencies.Ktor.callLogging)
    implementation(Dependencies.Ktor.callId)
    implementation(Dependencies.Ktor.swagger)
    implementation(Dependencies.Ktor.sessions)
    implementation(Dependencies.Ktor.auth)
    implementation(Dependencies.Ktor.authJwt)
    implementation(Dependencies.Ktor.netty)

    implementation(Dependencies.Database.Exposed.core)
    implementation(Dependencies.Database.Exposed.jdbc)

    implementation(Dependencies.Telemetry.micrometerRegistryPrometheus)

    implementation(Dependencies.Utils.logbackClassic)

    testImplementation(Dependencies.Ktor.serverTests)
    testImplementation(Dependencies.Test.kotlinTest)
}
