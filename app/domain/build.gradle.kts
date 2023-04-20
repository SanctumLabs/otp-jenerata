plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":app:core"))
    implementation(Dependencies.Utils.otpGenerator)
    implementation(Dependencies.Kotlin.X.datetime)
}
