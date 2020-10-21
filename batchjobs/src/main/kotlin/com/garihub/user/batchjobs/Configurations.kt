package com.garihub.user.batchjobs

import com.garihub.user.core.gateways.email.EmailGatewayApi
import com.garihub.user.core.usecases.SendEmailUseCase
import com.garihub.user.services.email.EmailGatewayApiImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configurations(
    @Value("\${services.email.url}")
    private val emailUrl: String
) {
    @Bean
    fun emailGatewayApi(): EmailGatewayApi {
        return EmailGatewayApiImpl(emailUrl = emailUrl)
    }

    @Bean
    fun sendEmailUseCase(emailGatewayApi: EmailGatewayApi): SendEmailUseCase {
        return SendEmailUseCase(emailGatewayApi)
    }
}
