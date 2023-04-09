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

    object Kotlin {
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KotlinVersion}"
    }

    object Test {
        const val mockK = "io.mockk:mockk:1.10.0"
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.KotlinVersion}"

        object Jupiter {
            private const val version = "5.8.2"
            const val engine = "org.junit.jupiter:junit-jupiter-engine:$version"
            const val vintageEngine = "org.junit.vintage:junit-vintage-engine"
            const val api = "org.junit.jupiter:junit-jupiter-api:$version"
        }

        object Spek {
            private const val version = "2.0.19"
            const val dslJvm = "org.spekframework.spek2:spek-dsl-jvm:$version"
            const val subjectExt = "org.jetbrains.spek:spek-subject-extension:$version"
            const val runnerJunit5 = "org.spekframework.spek2:spek-runner-junit5:$version"
        }

        object TestContainers {
            private const val version = "1.17.5"
            const val junitJupiter = "org.testcontainers:junit-jupiter:$version"
            const val postgresql = "org.testcontainers:postgresql:$version"
        }
    }

    object Telemetry {
        private const val prometheusVersion = "1.6.3"
        const val micrometerRegistryPrometheus = "io.micrometer:micrometer-registry-prometheus:$prometheusVersion"
    }
}
