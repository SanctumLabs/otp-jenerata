package com.sanctumlabs.otp.datastore

data class DatabaseParams(
    val driver: String,
    val url: String,
    val driverClass: String,
    val userName: String,
    val password: String,
    val cleanDB: Boolean = false,
)
