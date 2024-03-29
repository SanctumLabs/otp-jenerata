plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":app:core"))

    implementation(Dependencies.Database.hikariConnectionPooling)
    implementation(Dependencies.Database.postgres)
    implementation(Dependencies.Database.Exposed.core)
    implementation(Dependencies.Database.Exposed.jdbc)
    implementation(Dependencies.Database.Exposed.dao)
    implementation(Dependencies.Database.Exposed.javaTime)

    implementation(Dependencies.Kotlin.X.datetime)

    testImplementation(testFixtures(project(":libs:testfixtures")))
    testImplementation(Dependencies.DI.koinCore)
    testImplementation(Dependencies.DI.koinTest)
    testImplementation(Dependencies.DI.koinTestJunit5)
}
