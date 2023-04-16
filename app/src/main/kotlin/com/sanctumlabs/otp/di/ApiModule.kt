package com.sanctumlabs.otp.di

import com.sanctumlabs.otp.api.OtpService
import org.koin.dsl.module

val apiModule = module {
    single { OtpService(get(), get()) }
}
