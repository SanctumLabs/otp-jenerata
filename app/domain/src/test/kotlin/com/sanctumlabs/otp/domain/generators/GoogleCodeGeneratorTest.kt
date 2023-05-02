package com.sanctumlabs.otp.domain.generators

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals

class GoogleCodeGeneratorTest {
    private val key = "1234567"
    private val googleCodeGenerator by lazy {
        GoogleCodeGenerator(key)
    }

    @Test
    fun `should always generate a new OTP code for the different secrets`() {
        val secretOne = "secret"
        val secretTwo = "secretTwo"
        val actualOne = assertDoesNotThrow {
            googleCodeGenerator.generate(secretOne)
        }

        // new codes are generated every 30 seconds
        Thread.sleep(30000)

        val actualTwo = assertDoesNotThrow {
            googleCodeGenerator.generate(secretTwo)
        }

        assertNotEquals(actualOne.code, actualTwo.code)
    }
}
