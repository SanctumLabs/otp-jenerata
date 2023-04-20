package com.sanctumlabs.otp.core.services

import kotlinx.datetime.LocalDateTime


data class GeneratedOtpCode(
    val code: String,
    val expiryTime: LocalDateTime,
)

interface OtpCodeGenerator {
    fun generate(value: String): GeneratedOtpCode
}
