package com.sanctumlabs.otp

import com.sanctumlabs.otp.domain.BaseIntegrationTest
import com.sanctumlabs.otp.plugins.configureRouting
import com.sanctumlabs.otp.utils.testAppConfig
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
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
