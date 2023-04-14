package com.sanctumlabs.otp.config

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.config.ApplicationConfig

class ConfigImpl(private val config: ApplicationConfig) : Config {

    private val dotenv = dotenv {
        ignoreIfMissing = true
    }

    override fun getProperty(key: String, default: String): String {
        return dotenv.get(key, config.propertyOrNull(key)?.getString() ?: default)
    }

    override fun getProperty(key: String): String? {
        return dotenv.get(key, config.propertyOrNull(key)?.getString())
    }

    override fun getPropertyOrThrow(key: String) =
        getProperty(key) ?: throw IllegalStateException("Missing property $key")
}
