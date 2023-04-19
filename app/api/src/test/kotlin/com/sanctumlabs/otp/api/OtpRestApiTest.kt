package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpVerifyDto
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.UserId
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.time.LocalDateTime

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
        val expiryTime = LocalDateTime.now()
        val used = false
        val otpCode = OtpCode(code = code, userId = UserId(userId), expiryTime = expiryTime, used = used)

        every {
            mockOtpService.generateOtp(any())
        } returns otpCode

        val expectedResponse = """
            {
                "userId": "$userId",
                "code": "$code"
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

        verify {
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

        every {
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

        verify {
            mockOtpService.generateOtp(otpRequestDto = request)
        }

        confirmVerified(mockOtpService)
    }

    @Test
    fun `should return success response when there is a success verifying OTP code`() = testApplication {
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

        every {
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

        verify {
            mockOtpService.verifyOtp(verifyOtpDto = request)
        }

        confirmVerified(mockOtpService)
    }

    @Test
    fun `should return error response when there is a failure verifying OTP code`() = testApplication {
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

        val errorMessage = "Failed to verify OTP code $code"
        every {
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

        verify {
            mockOtpService.verifyOtp(verifyOtpDto = request)
        }

        confirmVerified(mockOtpService)
    }
}
