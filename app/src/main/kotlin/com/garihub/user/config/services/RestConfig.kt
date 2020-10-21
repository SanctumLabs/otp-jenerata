package com.garihub.user.config.services

import com.garihub.user.services.auth.AuthServiceApiInterceptor
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfig(
    @Value("\${services.auth.url}")
    private val authUrl: String,

    @Value("\${services.auth.masterRealm}")
    private val masterRealm: String,

    @Value("\${services.auth.admin.username}")
    private val username: String,

    @Value("\${services.auth.admin.password}")
    private val password: String,

    @Value("\${services.auth.admin.clientId}")
    private val clientId: String,

    @Value("\${services.auth.admin.grantType}")
    private val grantType: String
) {
    companion object {
        private const val CONNECTION_TIMEOUT = 15000
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean("customRestTemplate")
    fun customRestTemplate(): RestTemplate {
        val sslContext = SSLContextBuilder()
        sslContext.loadTrustMaterial(null, TrustSelfSignedStrategy())

        val httpClient: CloseableHttpClient = HttpClients.custom()
            .setSSLContext(sslContext.build())
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory()

        requestFactory.httpClient = httpClient
        requestFactory.setConnectTimeout(CONNECTION_TIMEOUT)

        return RestTemplate(requestFactory)
    }

    @Bean("authApiInterceptor")
    fun authApiInterceptor(): AuthServiceApiInterceptor {
        return AuthServiceApiInterceptor(
            authUrl = authUrl,
            masterRealm = masterRealm,
            username = username,
            password = password,
            clientId = clientId,
            grantType = grantType
        )
    }

    @Bean("authApiRestTemplate")
    fun authApiRestTemplate(authApiInterceptor: AuthServiceApiInterceptor): RestTemplate {
        val restTemplate = RestTemplate()

        var interceptors = restTemplate.interceptors

        if (interceptors.isEmpty()) {
            interceptors = arrayListOf()
        }

        interceptors.add(authApiInterceptor)
        restTemplate.interceptors = interceptors

        return restTemplate
    }
}
