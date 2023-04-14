package com.sanctumlabs.otp.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureLoggingPlugin(environment: ApplicationEnvironment) {

    install(CallLogging) {
        callIdMdc("call-id")
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, Http Method: $httpMethod, UserAgent: $userAgent"
        }

        filter { it.request.path().startsWith("/api/") }

        level = if (environment.developmentMode) Level.DEBUG else Level.INFO
    }
}
