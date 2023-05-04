package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpVerifyDto
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.UserId
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import io.mockk.confirmVerified
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@Tag("unit")
class OtpRestApiTest : KoinTest {
    private val mockOtpService = mockk<OtpService>()

    @BeforeEach
    fun before() {
        startKoin {
            modules(
                module {
                    single { mockOtpService }
                }
            )
        }
    }

    @AfterEach
    fun after() {
        stopKoin()
    }

    @Test
    fun `should return generated OTP code`() = testApplication {
        application {
            configureRouting()
        }

        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        val userId = "132456"
        val request = OtpRequestDto(userId)

        val code = "45468"
        val expiryTime = LocalDateTime(LocalDate(2023, 1, 1), LocalTime(1, 1, 1))
        val used = false
        val otpCode = OtpCode(code = code, userId = UserId(userId), expiryTime = expiryTime, used = used)

        coEvery {
            mockOtpService.generateOtp(any())
        } returns otpCode

        val expectedResponse = """
            {
                "userId": "$userId",
                "code": "$code",
                "expiryTime": "$expiryTime"
            }
        """.trimIndent()

        testHttpClient.post("/api/v1/otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
            .apply {
                assertEquals(HttpStatusCode.Created, status)
                assertEquals(expectedResponse, bodyAsText())
            }

        coVerify {
            mockOtpService.generateOtp(otpRequestDto = request)
        }

        confirmVerified(mockOtpService)
    }

    @Test
    fun `should return error when there is a failure generating OTP code`() = testApplication {
        application {
            configureRouting()
        }
        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        val userId = "132456"
        val request = OtpRequestDto(userId)

        coEvery {
            mockOtpService.generateOtp(any())
        } throws Exception("Failed to generated OTP code")

        val expectedResponse = """
            {
                "message": "Failed to generated OTP code"
            }
        """.trimIndent()

        testHttpClient.post("/api/v1/otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
            .apply {
                assertEquals(HttpStatusCode.InternalServerError, status)
                assertEquals(expectedResponse, bodyAsText())
            }

        coVerify {
            mockOtpService.generateOtp(otpRequestDto = request)
        }

        confirmVerified(mockOtpService)
    }

    @Test
    fun `should return success response when there is a success coVerifying OTP code`() = testApplication {
        application {
            configureRouting()
        }
        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        val userId = "132456"
        val code = "132456"
        val request = OtpVerifyDto(userId = userId, code = code)
        val verificationStatus = OtpVerificationStatus.VERIFIED

        coEvery {
            mockOtpService.verifyOtp(any())
        } returns verificationStatus

        val expectedResponse = """
            {
                "userId": "$userId",
                "code": "$code",
                "status": "$verificationStatus"
            }
        """.trimIndent()

        testHttpClient.post("/api/v1/otp/verify") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
            .apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(expectedResponse, bodyAsText())
            }

        coVerify {
            mockOtpService.verifyOtp(verifyOtpDto = request)
        }

        confirmVerified(mockOtpService)
    }

    @Test
    fun `should return error response when there is a failure coVerifying OTP code`() = testApplication {
        application {
            configureRouting()
        }
        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        val userId = "132456"
        val code = "132456"
        val request = OtpVerifyDto(userId = userId, code = code)

        val errorMessage = "Failed to coVerify OTP code $code"
        coEvery {
            mockOtpService.verifyOtp(any())
        } throws Exception(errorMessage)

        val expectedResponse = """
            {
                "message": "$errorMessage"
            }
        """.trimIndent()

        testHttpClient.post("/api/v1/otp/verify") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
            .apply {
                assertEquals(HttpStatusCode.InternalServerError, status)
                assertEquals(expectedResponse, bodyAsText())
            }

        coVerify {
            mockOtpService.verifyOtp(verifyOtpDto = request)
        }

        confirmVerified(mockOtpService)
    }
}
