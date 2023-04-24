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
    implementation(project(":app:api"))
    implementation(project(":app:core"))
    implementation(project(":app:datastore"))
    implementation(project(":app:domain"))

    implementation(Dependencies.Kotlin.X.datetime)

    implementation(Dependencies.Ktor.coreJvm)
    implementation(Dependencies.Ktor.openApi)
    implementation(Dependencies.Ktor.serializationJson)
    implementation(Dependencies.Ktor.serverContentNegotiation)
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
    implementation(Dependencies.Database.Exposed.core)
    implementation(Dependencies.Database.Exposed.jdbc)
    implementation(Dependencies.Database.Exposed.dao)

    implementation(Dependencies.Telemetry.micrometerRegistryPrometheus)
    implementation(Dependencies.Utils.logbackClassic)

    testImplementation(testFixtures(project(":libs:testfixtures")))
    testImplementation(Dependencies.Ktor.serverTests)
    testImplementation(Dependencies.DI.koinTest)
    testImplementation(Dependencies.DI.koinTestJunit5)
}
