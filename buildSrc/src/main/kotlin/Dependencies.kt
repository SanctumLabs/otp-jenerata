object Dependencies {
    object Database {
        const val postgres = "org.postgresql:postgresql"
        const val liquibaseCore = "org.liquibase:liquibase-core:3.6.3"
        const val flywayCore = "org.flywaydb:flyway-core:6.5.0"

        object Exposed {
            private const val version = "0.41.1"
            const val core = "org.jetbrains.exposed:exposed-core:$version"
            const val jdbc = "org.jetbrains.exposed:exposed-jdbc:$version"
        }
    }

    object Utils {
        const val lombok = "org.projectlombok:lombok"
        const val fasterXmlJacksonModule = "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9"

        object LibPhonenumber {
            const val core = "com.googlecode.libphonenumber:libphonenumber:8.12.8"
            const val geocoder = "com.googlecode.libphonenumber:geocoder:2.108"
            const val carrier = "com.googlecode.libphonenumber:carrier:1.99"
        }

        // Ref: https://github.com/marcelkliemannel/kotlin-onetimepassword
        const val otpGenerator = "dev.turingcomplete:kotlin-onetimepassword:2.4.0"
    }

    object Test {
        const val mockK = "io.mockk:mockk:1.10.0"
        const val jUnitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:5.0.1"
        const val jUnitJupiterApi = "org.junit.jupiter:junit-jupiter-api:5.0.1"
        const val spekApi = "org.jetbrains.spek:spek-api:2.0.11"
        const val spekDslJvm = "org.spekframework.spek2:spek-dsl-jvm:2.0.11"
        const val spekSubjectExt = "org.jetbrains.spek:spek-subject-extension:2.0.11"
        const val spekRunnerJunit5 = "org.spekframework.spek2:spek-runner-junit5:2.0.11"
    }
}
