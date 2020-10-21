package com.garihub.user.config.services

import com.garihub.user.core.gateways.email.EmailGatewayApi
import com.garihub.user.services.email.EmailGatewayApiImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailGatewayApiConfig(
    @Value("\${services.email.url}")
    private val emailUrl: String
) {

    @Bean
    fun emailGatewayApi(): EmailGatewayApi {
        return EmailGatewayApiImpl(emailUrl = emailUrl)
    }
}
