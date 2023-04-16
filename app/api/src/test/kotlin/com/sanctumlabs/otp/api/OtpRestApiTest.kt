package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import io.ktor.serialization.kotlinx.json.*
import io.mockk.mockk

class OtpRestApiTest {
    private val mockOtpService = mockk<OtpService>()

    @Test
    fun `should return generated OTP code`() = testApplication {
        val testHttpClient = createClient {
            install(plugin = ContentNegotiation) {
                json()
            }
        }

        application {
            routing {
                otpApiRoutes()
            }
        }


        val userId = "132456"

        testHttpClient.post("/api/v1/otp") {
            contentType(ContentType.Application.Json)
            setBody(OtpRequestDto(userId))
        }
            .apply {
                assertEquals(HttpStatusCode.Created, status)
                assertEquals("Hello, world!", bodyAsText())
            }
    }
}
