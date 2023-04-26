package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpResponseDto
import com.sanctumlabs.otp.api.dto.OtpVerifyDto
import com.sanctumlabs.otp.api.dto.OtpVerifyResponseDto
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.UserId
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
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.assertDoesNotThrow
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.days

@Tag("e2e")
class OtpRestApiE2eTest : BaseIntegrationTest(), KoinTest {

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

        val currentTime = Clock.System.now()
        val user = generateRandomString(10)
        val randomOtpCode = generateRandomString(6)
        val randomUserId = UserId(user)
        val futureExpiryTime = currentTime.plus(1.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val notUsed = false
        val otpCode =
            OtpCode(code = randomOtpCode, userId = randomUserId, expiryTime = futureExpiryTime, used = notUsed)

        // create an OTP record in the database
        val savedOtpRecord = transaction {
            OtpEntity.new {
                code = otpCode.code
                userId = otpCode.userId.value
                expiryTime = otpCode.expiryTime.toJavaLocalDateTime()
                used = otpCode.used
            }
        }

        assertNotNull(savedOtpRecord)
        assertEquals(savedOtpRecord.code, randomOtpCode)
        assertEquals(savedOtpRecord.used, notUsed)
        assertEquals(savedOtpRecord.userId, randomUserId.value)
        assertEquals(savedOtpRecord.expiryTime, futureExpiryTime.toJavaLocalDateTime())

        val request = OtpVerifyDto(userId = user, code = randomOtpCode)

        testHttpClient
            .post("/api/v1/otp/verify") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .apply {
                assertEquals(HttpStatusCode.OK, status)
                val actual = body<OtpVerifyResponseDto>()
                assertEquals(user, actual.userId)
                assertEquals(randomOtpCode, actual.code)
                assertEquals(OtpVerificationStatus.VERIFIED, actual.status)
            }

        // check that we actually updated the OTP record
        val updatedOtpRecord = transaction { OtpEntity.find { OtpTable.code eq randomOtpCode }.firstOrNull() }

        assertNotNull(updatedOtpRecord)
        assertEquals(updatedOtpRecord.code, randomOtpCode)
        assertEquals(updatedOtpRecord.used, true)
        assertEquals(updatedOtpRecord.userId, randomUserId.value)
        assertEquals(updatedOtpRecord.expiryTime, futureExpiryTime.toJavaLocalDateTime())
    }
}
