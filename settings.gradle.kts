rootProject.name = "otp-jenereta"
rootProject.buildFileName = "build.gradle.kts"

include(
    "app",
    "app:api",
    "app:core",
    "app:domain",
    "app:datastore",
    "libs:testfixtures"
)
