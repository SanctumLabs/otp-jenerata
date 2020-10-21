package com.garihub.user.services.email

import com.garihub.user.core.gateways.email.EmailGatewayApi
import com.garihub.user.services.email.dto.EmailDto
import com.garihub.user.services.email.dto.SuccessResponseDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate

class EmailGatewayApiImpl(private val emailUrl: String) : EmailGatewayApi {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val restTemplate = RestTemplate()

    @Suppress("TooGenericExceptionCaught")
    override fun sendEmail(emailAddress: String, subject: String, message: String): Boolean {
        return try {
            val email = EmailDto(
                to = arrayListOf(emailAddress),
                subject = subject,
                message = message
            )
            val request = HttpEntity(email)

            restTemplate.postForEntity(emailUrl, request, SuccessResponseDto::class.java)
            true
        } catch (e: Exception) {
            logger.error("Failed to send Email to $emailAddress. Err: ${e.message}")
            false
        }
    }
}
