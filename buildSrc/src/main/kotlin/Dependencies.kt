object Dependencies {
    object Ktor {
        private const val version = "2.2.4"
        const val coreJvm = "io.ktor:ktor-server-core-jvm:$version"
        const val openApi = "io.ktor:ktor-server-openapi:$version"
        const val contentNegotiation = "io.ktor:ktor-server-content-negotiation-jvm:$version"
        const val metrics = "io.ktor:ktor-server-metrics-jvm:$version"
        const val metricsMicrometer = "io.ktor:ktor-server-metrics-micrometer-jvm:$version"
        const val callLogging = "io.ktor:ktor-server-call-logging-jvm:$version"
        const val callId = "io.ktor:ktor-server-call-id-jvm:$version"
        const val swagger = "io.ktor:ktor-server-swagger:$version"
        const val sessions = "io.ktor:ktor-server-sessions-jvm:$version"
        const val auth = "io.ktor:ktor-server-auth-jvm:$version"
        const val authJwt = "io.ktor:ktor-server-auth-jwt-jvm:$version"
        const val netty = "io.ktor:ktor-server-netty-jvm:$version"

        const val serializationJson = "io.ktor:ktor-serialization-kotlinx-json-jvm:$version"

        const val serverTests = "io.ktor:ktor-server-tests-jvm:$version"
    }

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

        private const val logbackVersion = "1.2.11"
        const val logbackClassic = "ch.qos.logback:logback-classic:$logbackVersion"

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

        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.KotlinVersion}"
    }

    object Telemetry {
        private const val prometheusVersion = "1.6.3"
        const val micrometerRegistryPrometheus = "io.micrometer:micrometer-registry-prometheus:$prometheusVersion"
    }
}
