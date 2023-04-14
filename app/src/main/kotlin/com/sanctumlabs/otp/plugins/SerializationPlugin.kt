package com.sanctumlabs.otp.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.application.*
import kotlinx.serialization.json.Json


fun Application.configureSerializationPlugin() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
}
