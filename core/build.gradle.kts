plugins {
    kotlin("jvm")
    kotlin("plugin.noarg")
    kotlin("plugin.allopen")
}

dependencies {
    implementation(Libs.Utils.libPhoneNumber)
    implementation(Libs.Utils.libPhoneNumberCarrier)
    implementation(Libs.Utils.libPhoneNumberGeocoder)
}
