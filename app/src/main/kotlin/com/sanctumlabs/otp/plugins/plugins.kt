package com.sanctumlabs.otp.plugins

import io.ktor.server.application.*

fun Application.plugins() {
    configureHTTP()
    configureHeadersPlugin()
    configureSerializationPlugin()
    configureLoggingPlugin(environment)
    configureMonitoringPlugin()
    configureSecurity()
    configureRouting()
}
