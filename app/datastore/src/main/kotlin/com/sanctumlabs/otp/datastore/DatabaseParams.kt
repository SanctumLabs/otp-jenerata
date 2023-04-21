package com.sanctumlabs.otp.datastore

data class DatabaseParams(
    val driver: String,
    val url: String,
    val driverClass: String,
    val username: String,
    val password: String,
    val cleanDB: Boolean = false,
)
