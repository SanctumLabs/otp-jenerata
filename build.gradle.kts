import io.gitlab.arturbosch.detekt.Detekt
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
        classpath(Plugins.Jacoco.core)
    }
}

plugins {
    githooks
    id(Plugins.JvmTestSuite)
    kotlin("jvm") version Versions.KotlinVersion
    id(Plugins.Detekt.plugin) version Plugins.Detekt.version
}

allprojects {
    group = MetaInfo.GROUP

    apply(plugin = Plugins.Java)
    apply(plugin = Plugins.Jacoco.plugin)
    apply(plugin = Plugins.Detekt.plugin)
    apply(plugin = Plugins.JvmTestSuite)

    repositories {
        mavenCentral()
    }

    configure<JacocoPluginExtension> {
        toolVersion = Plugins.Jacoco.version
        reportsDirectory.set(file("${buildDir}/reports/jacoco"))
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

        withType<Detekt> {
            group = "linting"
            description = "linting task"
            exclude("**/resources/**", "**/build/**")
            autoCorrect = true
            config.setFrom(files("$rootDir/conf/linting/detekt/detekt.yml"))
            reports {
                html {
                    required.set(true)
                }
            }
        }

        withType(Test::class)
            .matching {
                it.name in listOf("unittests", "integrationtests", "e2e")
            }
            .configureEach {
                val testTypeName = this.name

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
                    html.outputLocation.set(file(project.rootDir.resolve("$buildDir/reports/tests/$testTypeName")))
                }

                configure<JacocoTaskExtension> {
                    isEnabled = true
                    classDumpDir = layout.buildDirectory.dir("jacoco/classpathdumps").get().asFile
                    reports {
                        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
                    }
                }

                finalizedBy("jacocoTestReport", "jacocoTestCoverageVerification")
            }

        withType<Test> {
            group = "tests"

            useJUnitPlatform {
                includeTags("integration", "unit", "e2e")
            }
        }

        register<Test>("unittests") {
            group = "tests"

            useJUnitPlatform {
                includeTags("unit")
            }
        }

        register<Test>("integrationtests") {
            group = "tests"

            useJUnitPlatform {
                includeTags("integration")
            }
        }

        register<Test>("e2e") {
            group = "tests"

            useJUnitPlatform {
                includeTags("e2e")
            }
        }

        withType<JacocoReport> {
            group = "tests"

            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(false)
                html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
                html.outputLocation.set(
                    file(project.rootDir.resolve("$buildDir/reports/coverage"))
                )
            }
        }

        withType<JacocoCoverageVerification> {
            group = "tests"
            description = "Test Coverage Verification"
            enabled = true
            violationRules {
                rule {
                    limit {
                        minimum = "0.5".toBigDecimal()
                    }
                }

                rule {
                    enabled = false
                    element = "CLASS"
                    includes = listOf("org.gradle.*")

                    limit {
                        counter = "LINE"
                        value = "TOTALCOUNT"
                        maximum = "0.3".toBigDecimal()
                    }
                }
            }
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8", Versions.KotlinVersion))
        detektPlugins(Dependencies.Utils.detektFormatting)
        testRuntimeOnly(Dependencies.Kotlin.reflect)
        testImplementation(Dependencies.Test.kotlinTest)
        testImplementation(Dependencies.Test.mockK)
        testImplementation(Dependencies.Test.Jupiter.api)
        testImplementation(Dependencies.Test.Jupiter.engine)
        testImplementation(Dependencies.Test.Spek.dslJvm)
        testImplementation(Dependencies.Kotlin.X.coroutinesCore)
    }
}
