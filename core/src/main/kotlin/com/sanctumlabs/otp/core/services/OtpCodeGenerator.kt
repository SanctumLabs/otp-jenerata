package com.sanctumlabs.otp.core.services

import java.time.LocalDateTime

data class GeneratedOtpCode(
    val code: String,
    val expiryTime: LocalDateTime,
)

interface OtpCodeGenerator {
    fun generate(value: String): GeneratedOtpCode
}
