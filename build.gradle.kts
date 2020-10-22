import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(Plugins.kotlinGradlePlugin)
        classpath(Plugins.kotlinGradlePluginAllOpen)
        classpath(Plugins.kotlinGradlePluginNoargs)
        classpath(Plugins.jacocoPlugin)
        classpath(Plugins.dokkaGradlePlugin)
    }
}

plugins {
    kotlin("jvm") version Versions.kotlinVersion
    kotlin("kapt") version Versions.kotlinVersion
    idea
    jacoco
    id(Plugins.detektPlugin) version Plugins.detektPluginVersion
    id(Plugins.palantirDockerPlugin) version Plugins.palantirDockerPluginVersion
    id(Plugins.sonarQubePlugin) version Plugins.sonarQubePluginVersion
    id(Plugins.dokkaPlugin) version Plugins.dokkaPluginVersion
}

apply(from = "githooks.gradle.kts")

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = Plugins.detektPlugin)
//    apply(plugin = Plugins.dokkaGradlePlugin)

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
    }

    configure<JacocoPluginExtension> {
        toolVersion = Plugins.jacocoVersion
    }

    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()
    }

    group = "com.garihub.otp"
    version = "0.0.1"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "${JavaVersion.VERSION_11}"
        targetCompatibility = "${JavaVersion.VERSION_11}"
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "${JavaVersion.VERSION_11}"
            }
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://jitpack.io")
        }
        jcenter()
        flatDir {
            dirs("libs")
        }
    }

    val userHome = System.getProperty("user.home")
    val moduleName = this.name

    tasks.getByName<JacocoReport>("jacocoTestReport") {
        reports {
            xml.isEnabled = false
            html.isEnabled = true
            html.destination =
                file(project.rootDir.resolve("$buildDir/reports/coverage"))
        }
        dependsOn("test")
    }

    tasks.withType<Test> {
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
            html.isEnabled = true
            html.destination = file(project.rootDir.resolve("$buildDir/reports/tests"))
        }
    }

    tasks.withType(DokkaTask::class.java) {
        // suppresses undocumented classes but not dokka warnings
        // https://github.com/Kotlin/dokka/issues/229 && https://github.com/Kotlin/dokka/issues/319
        //  reportUndocumented = false
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
        // Java 8 is only version supported both by Oracle/OpenJDK and Dokka itself
        // https://github.com/Kotlin/dokka/issues/294
        enabled = JavaVersion.current().isJava8
    }

    detekt {
        toolVersion = Plugins.detektPluginVersion
        description = "Runs detekt formatter"
        //  filters = ".*/resources/.*,.*/build/.*"
        config = files(
            project.rootDir.resolve("detekt/detekt.yml"),
            project.rootDir.resolve("detekt/failfast.yml")
        )
        parallel = true
        input = files("src/main/kotlin", "src/test/kotlin")
        debug = true
        reports {
            xml {
                enabled = false
            }
            html {
                enabled = true
                destination = file(project.rootDir.resolve("$buildDir/reports/detekt/$moduleName-report.html"))
            }
        }

        idea {
            path = "$userHome/.idea"
            codeStyleScheme = "$userHome/.idea/idea-code-style.xml"
            inspectionsProfile = "$userHome/.idea/inspect.xml"
            report = "project.projectDir/reports"
            mask = "*.kt"
        }
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8", Versions.kotlinVersion))

        testImplementation(Libs.Test.mockK)
        testImplementation(Libs.Test.jUnitJupiterApi)
        testImplementation(Libs.Test.jUnitJupiterEngine)
    }

    sourceSets["main"].java.srcDirs("src/main/kotlin")
}

tasks.withType<Wrapper> {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = gradleVersion
}

val copyApplicationBuild by tasks.registering(Copy::class) {
    group = "build"
    description = "Copies app build from app/config/build to $rootDir/app/build"
    from("$rootDir/app/build/.") {
        include("**/*")
        rename("app-(.*).jar", "otp-generator-$1.jar")
    }
    into("$rootDir/build/")
    dependsOn.add(setOf("app:bootJar"))
    mustRunAfter(":app:bootJar")
    doLast {
        logger.info(">> Done Copying app build")
    }
}

// ref https://github.com/palantir/gradle-docker
docker {
    name = "${System.getenv("CI_REGISTRY") ?: "garihublimited"}/otp-generator"
    setDockerfile(file("Dockerfile"))
    copySpec.from("app/build/libs/app-0.0.1.jar").into("build")
    buildArgs(mapOf("JAR_FILE" to "build/app-0.0.1.jar"))
}

// ref https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-gradle/
sonarqube {
    properties {
        property("sonar.host.url", System.getenv("SONAR_SERVER_URL") ?: "http:localhost:9000")
        property("sonar.projectDescription", "OTP Generator Service")
        property("sonar.java.coveragePlugin", "jacoco")
    }
}
