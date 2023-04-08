package com.sanctumlabs.otp.core.entities


/**
 * Representation of a User Verification OTP
 * @property otpCode [OtpCode] Passed in OTP code
 * @property channel [OtpChannel] Channel in which this OTP code is to be verified with
 */
data class VerifyOtpCode(
    val otpCode: String,
    val channel: OtpChannel
)
