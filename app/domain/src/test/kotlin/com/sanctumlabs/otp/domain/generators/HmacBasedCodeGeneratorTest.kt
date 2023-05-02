package com.sanctumlabs.otp.domain.generators

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class HmacBasedCodeGeneratorTest {
    private val codeDigits = 6
    private val key = "1234567"

    private val hmacConfig = HmacCodeGeneratorConfig(
        codeDigits = codeDigits,
        hmacAlgorithm = HmacAlgorithms.SHA1,
    )

    private val hmacBasedOtpCodeGenerator by lazy {
        HmacBasedCodeGenerator(key, hmacConfig)
    }

    @Test
    fun `should always generate a new OTP code for the different secrets`() {
        val secretOne = "secret"
        val secretTwo = "secretTwo"
        val actualOne = assertDoesNotThrow {
            hmacBasedOtpCodeGenerator.generate(secretOne)
        }

        val actualTwo = assertDoesNotThrow {
            hmacBasedOtpCodeGenerator.generate(secretTwo)
        }

        assertNotEquals(actualOne, actualTwo)
        assertEquals(codeDigits, actualOne.code.length)
        assertEquals(codeDigits, actualTwo.code.length)
    }
}
