package com.sanctumlabs.otp.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
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
