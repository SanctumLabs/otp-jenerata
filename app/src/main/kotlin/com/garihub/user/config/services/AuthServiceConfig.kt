package com.garihub.user.config.services

import com.garihub.user.core.gateways.auth.AuthServiceApi
import com.garihub.user.services.auth.AuthServiceApiImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AuthServiceConfig(
    @Value("\${services.auth.url}")
    private val authUrl: String,

    @Value("\${services.auth.riderRealm}")
    private val riderRealm: String,

    @Value("\${services.auth.driverRealm}")
    private val driverRealm: String
) {

    @Bean
    fun authServiceApi(@Qualifier("authApiRestTemplate") authRestTemplate: RestTemplate): AuthServiceApi {
        return AuthServiceApiImpl(
            authRestTemplate = authRestTemplate,
            authUrl = authUrl,
            riderRealm = riderRealm,
            driverRealm = driverRealm
        )
    }
}
