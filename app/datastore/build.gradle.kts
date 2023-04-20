plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":app:core"))

    implementation(Dependencies.Database.postgres)
    implementation(Dependencies.Database.Exposed.core)
    implementation(Dependencies.Database.Exposed.jdbc)
    implementation(Dependencies.Database.Exposed.dao)
    implementation(Dependencies.Database.Exposed.javaTime)

    implementation(Dependencies.Kotlin.X.datetime)
}
