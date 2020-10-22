package com.garihub.otp.config.core

import com.garihub.otp.core.gateways.datastore.DataStore
import com.garihub.otp.core.usecases.GenerateOtpUseCase
import com.garihub.otp.core.usecases.VerifyOtpUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfig(
    @Value("\${otp.key}")
    private val otpKey: String
) {

    @Bean
    fun generateOtpUseCase(dataStore: DataStore): GenerateOtpUseCase {
        return GenerateOtpUseCase(dataStore, otpKey)
    }

    @Bean
    fun getAUserUseCase(dataStore: DataStore): VerifyOtpUseCase {
        return VerifyOtpUseCase(dataStore)
    }
}
