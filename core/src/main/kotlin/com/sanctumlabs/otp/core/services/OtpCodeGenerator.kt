package com.sanctumlabs.otp.core.services

interface OtpCodeGenerator {
    fun generate(value: String): String
}
