plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(Dependencies.Utils.LibPhonenumber.core)
    implementation(Dependencies.Utils.LibPhonenumber.carrier)
    implementation(Dependencies.Utils.LibPhonenumber.geocoder)
    implementation(Dependencies.Utils.otpGenerator)

    testRuntimeOnly(Dependencies.Kotlin.reflect)
    testImplementation(Dependencies.Test.kotlinTest)
    testImplementation(Dependencies.Test.mockK)
    testImplementation(Dependencies.Test.Jupiter.api)
    testImplementation(Dependencies.Test.Jupiter.engine)
    testImplementation(Dependencies.Test.Spek.dslJvm)
    testImplementation(Dependencies.Test.Spek.runnerJunit5)
}
