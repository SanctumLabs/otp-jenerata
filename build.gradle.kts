import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(Plugins.KotlinGradlePlugin)
//        classpath(Plugins.JacocoPlugin)
    }
}

plugins {
    kotlin("jvm") version Versions.KotlinVersion
    id(Plugins.KtorPlugin) version Plugins.KtorPluginVersion
    id(Plugins.KotlinSerialization) version Versions.KotlinVersion
}


application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

allprojects {
    group = MetaInfo.GROUP

    repositories {
        mavenCentral()
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "${JavaVersion.VERSION_11}"
            }
        }

        withType<JavaCompile> {
            sourceCompatibility = "${JavaVersion.VERSION_11}"
            targetCompatibility = "${JavaVersion.VERSION_11}"
        }
    }
}

subprojects {

    repositories {
        mavenCentral()
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
            testLogging {
                // set options for log level LIFECYCLE
                events = setOf(
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED
                )
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
            }

            reports {
                html.required.set(true)
                html.outputLocation.set(file(project.rootDir.resolve("$buildDir/reports/tests")))
            }

//            configure<JacocoTaskExtension> {
//                isEnabled = true
//                classDumpDir = layout.buildDirectory.dir("jacoco/classpathdumps").get().asFile
//            }
        }

//        jacocoTestReport {
//            reports {
//                xml.required.set(true)
//                html.required.set(true)
//                csv.required.set(false)
//                html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
//                html.outputLocation.set(
//                    file(project.rootDir.resolve("$buildDir/reports/coverage"))
//                )
//            }
//            dependsOn("test")
//        }
    }

    dependencies {
//        implementation(kotlin("stdlib-jdk8"))
//        testRuntimeOnly(Dependencies.Kotlin.reflect)
//        testImplementation(Dependencies.Test.kotlinTest)
//        testImplementation(Dependencies.Test.mockK)
//        testImplementation(Dependencies.Test.jUnitJupiterApi)
//        testImplementation(Dependencies.Test.jUnitJupiterEngine)
//        testImplementation(Dependencies.Test.spekDsl)
//        testImplementation(Dependencies.Test.spekSubjectExt)
    }
}

dependencies {
    implementation(Dependencies.Ktor.coreJvm)
    implementation(Dependencies.Ktor.openApi)
    implementation(Dependencies.Ktor.serializationJson)
    implementation(Dependencies.Ktor.contentNegotiation)
    implementation(Dependencies.Ktor.metrics)
    implementation(Dependencies.Ktor.metricsMicrometer)
    implementation(Dependencies.Ktor.callLogging)
    implementation(Dependencies.Ktor.callId)
    implementation(Dependencies.Ktor.swagger)
    implementation(Dependencies.Ktor.sessions)
    implementation(Dependencies.Ktor.auth)
    implementation(Dependencies.Ktor.authJwt)
    implementation(Dependencies.Ktor.netty)

    implementation(Dependencies.Database.Exposed.core)
    implementation(Dependencies.Database.Exposed.jdbc)

    implementation(Dependencies.Telemetry.micrometerRegistryPrometheus)

    implementation(Dependencies.Utils.logbackClassic)
    testImplementation(Dependencies.Ktor.serverTests)
}
