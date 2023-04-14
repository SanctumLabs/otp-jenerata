package com.sanctumlabs.otp.plugins

import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    routing {
        openAPI(path = "openapi")
    }
    routing {
        swaggerUI(path = "openapi")
    }
}
