package com.sanctumlabs.otp.plugins

import com.sanctumlabs.otp.api.otpApiRoutes
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/health") {
            call.respondText("Healthy")
        }

        otpApiRoutes()
    }
}
