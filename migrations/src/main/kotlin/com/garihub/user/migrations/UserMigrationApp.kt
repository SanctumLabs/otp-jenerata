package com.garihub.user.migrations

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class UserMigrationApp

fun main(args: Array<String>) {
    SpringApplication.run(UserMigrationApp::class.java)
}
