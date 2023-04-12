plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))

    implementation(Dependencies.Database.postgres)
    implementation(Dependencies.Database.Exposed.core)
    implementation(Dependencies.Database.Exposed.jdbc)
    implementation(Dependencies.Database.Exposed.dao)
    implementation(Dependencies.Database.Exposed.javaTime)

    testRuntimeOnly(Dependencies.Kotlin.reflect)
    testImplementation(Dependencies.Test.kotlinTest)
    testImplementation(Dependencies.Test.mockK)
    testImplementation(Dependencies.Test.Jupiter.api)
    testImplementation(Dependencies.Test.Jupiter.engine)
    testImplementation(Dependencies.Test.Spek.dslJvm)
    testImplementation(Dependencies.Test.Spek.runnerJunit5)
}
