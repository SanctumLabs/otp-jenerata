package com.garihub.otp.core.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UtilsTest {

    private val alphanumericRegex = "[a-zA-Z0-9]+"

    @Test
    fun `Should always generate a unique identifier`() {
        val actualOne = generateIdentifier("rice")
        val actualTwo = generateIdentifier("ugali")
        Assertions.assertNotEquals(actualOne, actualTwo)
    }

    @Test
    fun `Should always generate a random identifier that matches alphanumeric pattern`() {
        val actual = generateIdentifier("beans")

        Assertions.assertTrue(actual.matches(Regex(alphanumericRegex)))
    }
}
