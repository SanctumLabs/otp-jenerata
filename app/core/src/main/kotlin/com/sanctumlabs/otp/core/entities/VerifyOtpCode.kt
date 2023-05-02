package com.sanctumlabs.otp.core.entities

/**
 * Representation of a User Verification OTP
 * @property otpCode [OtpCode] Passed in OTP code
 * @property userId [UserId] Channel in which this OTP code is to be verified with
 */
data class VerifyOtpCode(
    val otpCode: String,
    val userId: UserId
)
