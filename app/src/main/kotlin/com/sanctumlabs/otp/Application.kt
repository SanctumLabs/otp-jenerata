package com.sanctumlabs.otp

import com.sanctumlabs.otp.di.modules
import com.sanctumlabs.otp.plugins.plugins
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

// application.conf references the main function. This annotation prevents the IDE from marking it as unused.
@Suppress("unused")
fun Application.module() {
    plugins()
    modules(environment)
}
