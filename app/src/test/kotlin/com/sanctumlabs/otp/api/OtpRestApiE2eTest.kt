package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpResponseDto
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import com.sanctumlabs.otp.di.modules
import com.sanctumlabs.otp.domain.BaseIntegrationTest
import com.sanctumlabs.otp.plugins.configureHeadersPlugin
import com.sanctumlabs.otp.plugins.configureRouting
import com.sanctumlabs.otp.plugins.configureSerializationPlugin
import com.sanctumlabs.otp.testfixtures.utils.generateRandomString
import com.sanctumlabs.otp.utils.testAppConfig
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.assertNotNull

@Tag("e2e")
class OtpRestApiE2eTest : BaseIntegrationTest() {

    @Test
    fun `should be able to create an OTP record for a user ID`() = testApplication {
        environment {
            config = testAppConfig
        }

        application {
            modules(this.environment)
            configureRouting()
            configureHeadersPlugin()
            configureSerializationPlugin()
        }

        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        val userId = generateRandomString()
        val request = OtpRequestDto(userId)

        testHttpClient
            .post("/api/v1/otp") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .apply {
                assertEquals(HttpStatusCode.Created, status)
                val actual = body<OtpResponseDto>()
                assertEquals(userId, actual.userId)
                assertNotNull(actual.code)
                assertNotNull(actual.expiryTime)
            }

        // assert that an OTP code was created with the given user ID
        val actual = transaction { OtpEntity.find { OtpTable.userId eq userId }.firstOrNull() }

        assertNotNull(actual)
        assertEquals(actual.code.length, 6)
        assertEquals(actual.used, false)
        assertEquals(actual.userId, userId)
    }

    @Test
    fun `should be able to verify an OTP record for a user ID`() = testApplication {
        environment {
            config = testAppConfig
        }

        application {
            configureRouting()
            configureHeadersPlugin()
            configureSerializationPlugin()
        }

        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        val userId = generateRandomString()
        val request = OtpRequestDto(userId)

        testHttpClient
            .post("/api/v1/otp") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .apply {
                assertEquals(HttpStatusCode.Created, status)
                val actual = body<OtpResponseDto>()
                assertEquals(userId, actual.userId)
                assertNotNull(actual.code)
                assertNotNull(actual.expiryTime)
            }

        // assert that an OTP code was created with the given user ID
        val actual = transaction { OtpEntity.find { OtpTable.userId eq userId }.firstOrNull() }

        assertNotNull(actual)
        assertEquals(actual.code.length, 6)
        assertEquals(actual.used, false)
        assertEquals(actual.userId, userId)
    }
}
