plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Dependencies.Utils.LibPhonenumber.core)
    implementation(Dependencies.Utils.LibPhonenumber.carrier)
    implementation(Dependencies.Utils.LibPhonenumber.geocoder)
    implementation(Dependencies.Utils.otpGenerator)
    implementation(kotlin("stdlib-jdk8"))
}
