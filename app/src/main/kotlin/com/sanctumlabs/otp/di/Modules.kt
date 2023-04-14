package com.sanctumlabs.otp.di

import io.ktor.server.application.*
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

fun modules(environment: ApplicationEnvironment) {
    startKoin {
        modules(
            appModule(environment)
        )
    }

    loadKoinModules(
        listOf(
            apiModule,
            databaseModule,
            domainModule
        )
    )
}
