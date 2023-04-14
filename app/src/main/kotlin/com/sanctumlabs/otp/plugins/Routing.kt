package com.sanctumlabs.otp.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    install(Routing)

    routing {
        get("/health") {
            call.respondText("Healthy")
        }
    }
}
