plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":core"))
    implementation(Dependencies.Utils.LibPhonenumber.core)
    implementation(Dependencies.Utils.LibPhonenumber.carrier)
    implementation(Dependencies.Utils.LibPhonenumber.geocoder)
    implementation(Dependencies.Utils.otpGenerator)
}
