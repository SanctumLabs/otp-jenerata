package com.garihub.user.services.auth

import com.garihub.user.core.EmailAddress
import com.garihub.user.core.UserExternalIdentifier
import com.garihub.user.services.auth.dto.CredentialsRepresentationDto
import com.garihub.user.services.auth.dto.UserRepresentationDto
import com.garihub.user.core.exceptions.AuthException
import com.garihub.user.core.gateways.auth.AuthServiceApi
import com.garihub.user.core.models.Gender
import com.garihub.user.core.models.UserAttributes
import com.garihub.user.services.auth.dto.UserUpdateDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate

class AuthServiceApiImpl(
    private val authRestTemplate: RestTemplate,
    private val authUrl: String,
    private val riderRealm: String,
    private val driverRealm: String
) : AuthServiceApi {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Suppress("ReturnCount")
    private fun getUser(email: EmailAddress, realm: String): UserRepresentationDto? {
        @Suppress("TooGenericExceptionCaught")
        return try {
            val response =
                authRestTemplate.getForEntity(
                    "$authUrl/admin/realms/$realm/users?email=$email",
                    Array<UserRepresentationDto>::class.java
                )
            val responseBody = response.body ?: return null

            return try {
                responseBody.first { it.email == email }
            } catch (nsee: NoSuchElementException) {
                null
            }
        } catch (e: Exception) {
            logger.error("Failed to get user $email on realm $realm. Err: ${e.message}")
            null
        }
    }

    private fun isUserRegistered(email: String, realm: String): Boolean {
        val response = getUser(email, realm)
        return response != null
    }

    override fun isRiderUserRegistered(email: String): Boolean {
        @Suppress("TooGenericExceptionCaught")
        return try {
            isUserRegistered(email, riderRealm)
        } catch (e: Exception) {
            logger.error("Failed to check rider registration. Err: ${e.message}")
            false
        }
    }

    override fun registerRiderUser(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        gender: Gender
    ): Boolean {
        val credentials = CredentialsRepresentationDto(
            value = password,
            type = CREDENTIAL_PASSWORD_TYPE,
            credentialData = password
        )

        val user = UserRepresentationDto(
            firstName = firstName,
            lastName = lastName,
            email = email,
            username = email,
            emailVerified = false,
            enabled = false,
            credentials = arrayListOf(credentials),
            attributes = hashMapOf(
                UserAttributes.PHONE_NUMBER.name to phoneNumber,
                UserAttributes.GENDER.name to gender.name
            )
        )

        val request = HttpEntity(user)

        @Suppress("TooGenericExceptionCaught")
        return try {
            authRestTemplate.postForEntity(
                "$authUrl/admin/realms/$riderRealm/users",
                request,
                Any::class.java
            )
            true
        } catch (e: Exception) {
            logger.error("Failed to register rider. Err: ${e.message}")
            throw AuthException("Failed to register rider. Err: ${e.message}")
        }
    }

    override fun verifyRiderEmailAddress(email: EmailAddress): UserExternalIdentifier? {
        val user = getUser(email, riderRealm) ?: return null

        val userUpdate = UserUpdateDto(emailVerified = true)

        val request = HttpEntity(userUpdate)

        @Suppress("TooGenericExceptionCaught")
        return try {
            authRestTemplate.put(
                "$authUrl/admin/realms/$riderRealm/users/${user.id}",
                request,
                Any::class.java
            )
            user.id
        } catch (e: Exception) {
            logger.error("Failed to verify rider email address. Err: ${e.message}")
            throw AuthException("Failed to verify rider email address")
        }
    }
}
