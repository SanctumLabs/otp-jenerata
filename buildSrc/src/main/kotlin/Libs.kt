object Libs {
    object SpringBoot {
        const val actuator = "org.springframework.boot:spring-boot-starter-actuator"
        const val dataJpa = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val starterWeb = "org.springframework.boot:spring-boot-starter-web"
        const val starterJdbc = "org.springframework.boot:spring-boot-starter-jdbc"
        const val starter = "org.springframework.boot:spring-boot-starter"
        const val springFoxSwaggerUi = "io.springfox:springfox-swagger-ui:2.9.2"
        const val springFoxSwagger2 = "io.springfox:springfox-swagger2:2.9.2"
        const val thymeleaf = "org.springframework.boot:spring-boot-starter-thymeleaf:${Versions.springBootVersion}"
        const val web = "org.springframework.boot:spring-boot-starter-web:${Versions.springBootVersion}"
        const val batch = "org.springframework.boot:spring-boot-starter-batch:${Versions.springBootVersion}"

        object Test {
            const val starterTest = "org.springframework.boot:spring-boot-starter-test"
        }

        object Tools {
            const val devTools = "org.springframework.boot:spring-boot-devtools"
        }
    }

    object Data {
        const val postgres = "org.postgresql:postgresql"
        const val springBootJdbc = "org.springframework.boot:spring-boot-starter-jdbc:${Versions.springBootVersion}"
        const val liquibaseCore = "org.liquibase:liquibase-core:3.6.3"
        const val flywayCore = "org.flywaydb:flyway-core:6.5.0"
        const val mysqlDbJdbc = "mysql:mysql-connector-java:8.0.20"
        const val h2Database = "com.h2database:h2:1.4.197"
    }

    object Utils {
        const val lombok = "org.projectlombok:lombok"
        const val fasterXmlJacksonModule = "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9"
        const val javaxValidation = "javax.validation:validation-api:2.0.0.Final"
        const val libPhoneNumber = "com.googlecode.libphonenumber:libphonenumber:8.12.8"
        const val libPhoneNumberGeocoder = "com.googlecode.libphonenumber:geocoder:2.108"
        const val libPhoneNumberCarrier = "com.googlecode.libphonenumber:carrier:1.99"
        const val thymeleaf = "org.springframework.boot:spring-boot-starter-thymeleaf:${Versions.springBootVersion}"
    }

    object Network {
        const val apacheHttpClient = "org.apache.httpcomponents:httpclient:4.5.12"
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
