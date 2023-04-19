plugins {
    kotlin("jvm") version Versions.KotlinVersion
    id(Plugins.KotlinSerialization) version Versions.KotlinVersion
    id(Plugins.Ktor.plugin) version Plugins.Ktor.version
}

dependencies {
    implementation(project(":app:core"))

    implementation(Dependencies.Ktor.coreJvm)
    implementation(Dependencies.Ktor.auth)
    implementation(Dependencies.Ktor.authJwt)
    implementation(Dependencies.Ktor.netty)
    implementation(Dependencies.Ktor.serializationJson)

    implementation(Dependencies.DI.koinKtor)

    testImplementation(Dependencies.Ktor.serverContentNegotiation)
    testImplementation(Dependencies.Ktor.clientContentNegotiation)
    testImplementation(Dependencies.Ktor.serverTestHost)
    testImplementation(Dependencies.DI.koinTest)
    testImplementation(Dependencies.DI.koinTestJunit5)
}
