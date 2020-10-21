package com.garihub.user.services.auth

import com.garihub.user.services.auth.dto.AuthTokenResponse
import com.garihub.user.core.exceptions.AuthException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

class AuthServiceApiInterceptor(
    private val authUrl: String,
    private val masterRealm: String,
    private val username: String,
    private val password: String,
    private val clientId: String,
    private val grantType: String = "password"
) : ClientHttpRequestInterceptor {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val restTemplate = RestTemplate()

    private fun authenticate(): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map = LinkedMultiValueMap<String, String>()

        map.add("username", username)
        map.add("password", password)
        map.add("grant_type", grantType)
        map.add("client_id", clientId)

        val request = HttpEntity(map, headers)
        try {
            val response = restTemplate.exchange(
                "$authUrl/realms/$masterRealm/protocol/openid-connect/token",
                HttpMethod.POST,
                request,
                AuthTokenResponse::class.java
            )
            val responseBody = response.body
                ?: throw AuthException(
                    "Unavailable Token Response Status: ${response.statusCodeValue}"
                )
            return responseBody.accessToken
        } catch (e: RestClientException) {
            logger.error("Failed auth with AuthService ${e.message}")
            throw AuthException("Failed auth with AuthService ${e.message}")
        }
    }

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val token = authenticate()
        request.headers.setBearerAuth(token)
        return execution.execute(request, body)
    }
}
