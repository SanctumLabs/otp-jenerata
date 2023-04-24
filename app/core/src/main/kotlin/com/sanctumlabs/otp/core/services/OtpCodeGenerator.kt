package com.sanctumlabs.otp.core.services

import kotlinx.datetime.LocalDateTime


/**
 * Generated OTP code
 * @param code [String] Generated OTP code value
 * @param expiryTime [LocalDateTime] When the OTP code is set to expire
 */
data class GeneratedOtpCode(
    val code: String,
    val expiryTime: LocalDateTime,
)

interface OtpCodeGenerator {

    /**
     * @param value [String] Value to generate OTP code for
     * @return [GeneratedOtpCode] Generated OTP code for given value
     */
    fun generate(value: String): GeneratedOtpCode
}
