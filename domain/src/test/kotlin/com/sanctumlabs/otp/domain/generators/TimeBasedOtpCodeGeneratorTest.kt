package com.sanctumlabs.otp.domain.generators

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotEquals

class TimeBasedOtpCodeGeneratorTest {
    private val key = "1234567"
    private val config = TimeBaseCodeGeneratorConfig(
        codeDigits = 6,
        hmacAlgorithm = HmacAlgo.SHA1,
        timeStep = 180,
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
    }
}
