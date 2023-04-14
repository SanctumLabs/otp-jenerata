package com.sanctumlabs.otp.di

import com.sanctumlabs.otp.config.Config
import com.sanctumlabs.otp.config.ConfigImpl
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module

fun appModule(environment: ApplicationEnvironment) = module {
    single { environment.log }
    single<Config> {
        val appConfig = HoconApplicationConfig(ConfigFactory.load())
        ConfigImpl(appConfig)
    }
}
