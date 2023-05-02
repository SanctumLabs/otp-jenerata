package com.sanctumlabs.otp.plugins

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.defaultheaders.DefaultHeaders

fun Application.configureHeadersPlugin() {

    install(DefaultHeaders) {
//        header(HttpHeaders.AccessControlAllowOrigin, "*")
        header(HttpHeaders.Server, "N/A for security reasons")
        header(HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
        header(HttpHeaders.Pragma, "no-cache")
        header(HttpHeaders.Expires, "0")
    }
}
