package com.sanctumlabs.otp.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.http.HttpHeaders

fun Application.configureHeadersPlugin() {

    install(DefaultHeaders) {
//        header(HttpHeaders.AccessControlAllowOrigin, "*")
        header(HttpHeaders.Server, "N/A for security reasons")
        header(HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
        header(HttpHeaders.Pragma, "no-cache")
        header(HttpHeaders.Expires, "0")
    }
}
