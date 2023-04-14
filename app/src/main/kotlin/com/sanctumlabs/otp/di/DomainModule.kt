package com.sanctumlabs.otp.di

import com.sanctumlabs.otp.core.services.CreateOtpService
import com.sanctumlabs.otp.core.services.OtpCodeGenerator
import com.sanctumlabs.otp.core.services.VerifyOtpService
import com.sanctumlabs.otp.domain.generators.GoogleCodeGenerator
import com.sanctumlabs.otp.domain.generators.HmacBasedCodeGenerator
import com.sanctumlabs.otp.domain.generators.TimeBasedOtpCodeGenerator
import com.sanctumlabs.otp.domain.services.CreateOtpServiceImpl
import com.sanctumlabs.otp.domain.services.VerifyOtpServiceImpl
import org.koin.dsl.module

val domainModule = module {
    single<OtpCodeGenerator> { TimeBasedOtpCodeGenerator(get(), get()) }
    single<OtpCodeGenerator> { HmacBasedCodeGenerator(get(), get()) }
    single<OtpCodeGenerator> { GoogleCodeGenerator(get()) }

    single<CreateOtpService> { CreateOtpServiceImpl(get(), get()) }
    single<VerifyOtpService> { VerifyOtpServiceImpl(get()) }
}
