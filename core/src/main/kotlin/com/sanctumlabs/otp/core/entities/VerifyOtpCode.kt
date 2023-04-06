package com.sanctumlabs.otp.core.entities


/**
 * Representation of a User Verification OTP
 * @property otpCode [OtpCode] Passed in OTP code
 * @property phoneNumberOrEmail [PhoneNumberOrEmail] User's phone number
 */
data class VerifyOtpCode(
    val otpCode: String,
    val phoneNumberOrEmail: PhoneNumberOrEmail
)
