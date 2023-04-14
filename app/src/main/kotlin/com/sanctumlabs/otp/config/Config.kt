package com.sanctumlabs.otp.config

interface Config {
    fun getProperty(key: String, default: String): String

    fun getProperty(key: String): String?

    fun getPropertyOrThrow(key: String): String
}
