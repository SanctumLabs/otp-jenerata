package com.garihub.otp.migrations

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class OtpGenMigrationApp

fun main(args: Array<String>) {
    SpringApplication.run(OtpGenMigrationApp::class.java)
}
