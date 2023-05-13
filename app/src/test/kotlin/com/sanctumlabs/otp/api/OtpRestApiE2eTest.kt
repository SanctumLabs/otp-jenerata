package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpResponseDto
import com.sanctumlabs.otp.api.dto.OtpVerifyDto
import com.sanctumlabs.otp.api.dto.OtpVerifyResponseDto
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import com.sanctumlabs.otp.di.apiModule
import com.sanctumlabs.otp.di.configModule
import com.sanctumlabs.otp.di.databaseModule
import com.sanctumlabs.otp.di.domainModule
import com.sanctumlabs.otp.domain.BaseIntegrationTest
import com.sanctumlabs.otp.plugins.configureHeadersPlugin
import com.sanctumlabs.otp.plugins.configureRouting
import com.sanctumlabs.otp.plugins.configureSerializationPlugin
import com.sanctumlabs.otp.testfixtures.utils.generateRandomString
import com.sanctumlabs.otp.utils.testAppConfig
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.assertNotNull

@Tag("e2e")
class OtpRestApiE2eTest : BaseIntegrationTest(), KoinTest {

    @AfterEach
    override fun teardown() {
        super.teardown()
        stopKoin()
    }

    @Test
    fun `should be able to create an OTP record for a user ID`() = testApplication {
        environment {
            config = testAppConfig
        }

        application {
            startKoin {
                modules(
                    configModule,
                )
                loadKoinModules(
                    listOf(
                        apiModule,
                        databaseModule,
                        domainModule
                    )
                )
            }
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
    @Suppress("LongMethod")
    fun `should be able to verify an OTP record for a user ID`() = testApplication {
        environment {
            config = testAppConfig
        }

        application {
            startKoin {
                modules(
                    configModule,
                )
                loadKoinModules(
                    listOf(
                        apiModule,
                        databaseModule,
                        domainModule
                    )
                )
            }
            configureRouting()
            configureHeadersPlugin()
            configureSerializationPlugin()
        }

        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        val user = generateRandomString(10)

        val otpRequest = OtpRequestDto(user)

        testHttpClient
            .post("/api/v1/otp") {
                contentType(ContentType.Application.Json)
                setBody(otpRequest)
            }
            .apply {
                assertEquals(HttpStatusCode.Created, status)
                val actual = body<OtpResponseDto>()
                assertEquals(user, actual.userId)
                assertNotNull(actual.code)
                assertNotNull(actual.expiryTime)
            }

        val savedOtpEntity = transaction { OtpEntity.find { OtpTable.userId eq user }.firstOrNull() }

        assertNotNull(savedOtpEntity)

        val actualCode = savedOtpEntity.code
        val request = OtpVerifyDto(userId = user, code = actualCode)

        testHttpClient
            .post("/api/v1/otp/verify") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .apply {
                assertEquals(HttpStatusCode.OK, status)

                val actualResponse = body<OtpVerifyResponseDto>()

                assertEquals(user, actualResponse.userId)
                assertEquals(actualCode, actualResponse.code)
                assertEquals(OtpVerificationStatus.VERIFIED, actualResponse.status)
            }

        val updatedOtpEntity = transaction { OtpEntity.find { OtpTable.userId eq user }.firstOrNull() }

        assertNotNull(updatedOtpEntity)
        assertEquals(true, updatedOtpEntity.used)
    }
}
