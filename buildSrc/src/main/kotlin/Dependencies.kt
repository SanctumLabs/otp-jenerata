object Dependencies {
    object Ktor {
        private const val version = "2.2.4"
        const val coreJvm = "io.ktor:ktor-server-core-jvm:$version"
        const val openApi = "io.ktor:ktor-server-openapi:$version"
        const val serverContentNegotiation = "io.ktor:ktor-server-content-negotiation:$version"
        const val clientContentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
        const val metrics = "io.ktor:ktor-server-metrics-jvm:$version"
        const val metricsMicrometer = "io.ktor:ktor-server-metrics-micrometer-jvm:$version"
        const val callLogging = "io.ktor:ktor-server-call-logging-jvm:$version"
        const val callId = "io.ktor:ktor-server-call-id-jvm:$version"
        const val swagger = "io.ktor:ktor-server-swagger:$version"
        const val sessions = "io.ktor:ktor-server-sessions-jvm:$version"
        const val auth = "io.ktor:ktor-server-auth-jvm:$version"
        const val authJwt = "io.ktor:ktor-server-auth-jwt-jvm:$version"
        const val netty = "io.ktor:ktor-server-netty-jvm:$version"
        const val defaultHeaders = "io.ktor:ktor-server-default-headers:$version"

        const val serializationJson = "io.ktor:ktor-serialization-kotlinx-json-jvm:$version"

        const val serverTests = "io.ktor:ktor-server-tests-jvm:$version"
        const val serverTestHost = "io.ktor:ktor-server-test-host:$version"
    }

    object DI {
        private const val koinVersion = "3.4.0"
        const val koinCore = "io.insert-koin:koin-core:$koinVersion"
        const val koinLogger = "io.insert-koin:koin-logger-slf4j:$koinVersion"
        const val koinKtor = "io.insert-koin:koin-ktor:$koinVersion"
        const val koinTest = "io.insert-koin:koin-test:$koinVersion"
        const val koinTestJunit5 = "io.insert-koin:koin-test-junit5:$koinVersion"
    }

    object Database {
        const val postgres = "org.postgresql:postgresql:42.6.0"

        const val liquibaseCore = "org.liquibase:liquibase-core:3.6.3"
        const val flywayCore = "org.flywaydb:flyway-core:6.5.0"

        // HikariCP (Connection Pooling)
        const val hikariConnectionPooling = "com.zaxxer:HikariCP:5.0.1"

        object Exposed {
            private const val version = "0.41.1"
            const val core = "org.jetbrains.exposed:exposed-core:$version"
            const val jdbc = "org.jetbrains.exposed:exposed-jdbc:$version"
            const val dao = "org.jetbrains.exposed:exposed-dao:$version"
            const val javaTime = "org.jetbrains.exposed:exposed-java-time:$version"
            const val kotlinTime = "org.jetbrains.exposed:exposed-kotlin-datetime:$version"
        }
    }

    object Utils {
        const val lombok = "org.projectlombok:lombok"
        const val fasterXmlJacksonModule = "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9"

        private const val logbackVersion = "1.2.11"
        const val logbackClassic = "ch.qos.logback:logback-classic:$logbackVersion"

        const val dotenv = "io.github.cdimascio:dotenv-kotlin:6.4.1"

        // Ref: https://github.com/marcelkliemannel/kotlin-onetimepassword
        const val otpGenerator = "dev.turingcomplete:kotlin-onetimepassword:2.4.0"
    }

    object Kotlin {
        object X {
            const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
            const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-RC"
        }

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
            private const val version = "1.18.0"
            const val junitJupiter = "org.testcontainers:junit-jupiter:$version"
            const val postgresql = "org.testcontainers:postgresql:$version"
        }
    }

    object Telemetry {
        private const val prometheusVersion = "1.6.3"
        const val micrometerRegistryPrometheus = "io.micrometer:micrometer-registry-prometheus:$prometheusVersion"
    }
}
