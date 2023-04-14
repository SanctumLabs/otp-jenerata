package com.sanctumlabs.otp.domain.generators

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TimeBasedOtpCodeGeneratorTest {
    private val key = "1234567"
    private val codeDigits = 6
    private val timeStep = 180L

    private val config = TimeBasedCodeGeneratorConfig(
        codeDigits = codeDigits,
        hmacAlgorithm = HmacAlgorithms.SHA1,
        timeStep = timeStep,
        timeStepUnit = TimeUnit.SECONDS
    )

    private val timeBasedOtpCodeGenerator by lazy {
        TimeBasedOtpCodeGenerator(key, config)
    }

    @Test
    fun `should always generate a new OTP code for the different secrets`() {
        val secretOne = "secret"
        val secretTwo = "secretTwo"
        val actualOne = assertDoesNotThrow {
            timeBasedOtpCodeGenerator.generate(secretOne)
        }

        val actualTwo = assertDoesNotThrow {
            timeBasedOtpCodeGenerator.generate(secretTwo)
        }

        assertNotEquals(actualOne, actualTwo)
        assertEquals(codeDigits, actualOne.code.length)
        assertEquals(codeDigits, actualTwo.code.length)
    }
}
