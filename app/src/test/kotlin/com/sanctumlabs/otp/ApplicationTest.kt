package com.sanctumlabs.otp

import com.sanctumlabs.otp.domain.BaseIntegrationTest
import com.sanctumlabs.otp.plugins.configureRouting
import com.sanctumlabs.otp.utils.testAppConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("integration")
class ApplicationTest : BaseIntegrationTest() {
    @Test
    fun testRoot() = testApplication {
        environment {
            config = testAppConfig
        }

        application {
            configureRouting()
        }
        client.get("/health").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Healthy", bodyAsText())
        }
    }
}
