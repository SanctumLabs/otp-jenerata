package com.sanctumlabs.otp.plugins

import com.sanctumlabs.otp.api.otpApiRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/health") {
            call.respondText("Healthy")
        }

        otpApiRoutes()
    }
}
