plugins {
    kotlin("jvm") version Versions.KotlinVersion
    id(Plugins.KotlinSerialization) version Versions.KotlinVersion
    id(Plugins.Ktor.plugin) version Plugins.Ktor.version
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
//    implementation(project(":api"))
    implementation(project(":app:core"))
    implementation(project(":app:datastore"))
    implementation(project(":app:domain"))

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
    implementation(Dependencies.Ktor.defaultHeaders)

    implementation(Dependencies.DI.koinCore)
    implementation(Dependencies.DI.koinKtor)
    implementation(Dependencies.DI.koinLogger)

    implementation(Dependencies.Utils.dotenv)

    implementation(Dependencies.Database.hikariConnectionPooling)

    implementation(Dependencies.Telemetry.micrometerRegistryPrometheus)

    implementation(Dependencies.Utils.logbackClassic)
    testImplementation(Dependencies.Ktor.serverTests)
}

