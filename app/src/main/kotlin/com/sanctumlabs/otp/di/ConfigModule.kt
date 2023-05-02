package com.sanctumlabs.otp.di

import com.sanctumlabs.otp.config.Config
import com.sanctumlabs.otp.config.ConfigImpl
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import org.koin.dsl.module

val configModule = module {
    single<Config> {
        val appConfig = HoconApplicationConfig(ConfigFactory.load())
        ConfigImpl(appConfig)
    }
}
