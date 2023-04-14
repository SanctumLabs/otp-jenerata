package com.sanctumlabs.otp.database

interface DatabaseConfig {
    val driver: String

    val url: String

    val driverClass: String

    val userName: String

    val password: String
}
