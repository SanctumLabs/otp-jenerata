package com.sanctumlabs.otp.core.entities

import java.time.LocalDateTime

/**
 * Representation of a User OTP in the platform
 * @property code [String] Generated OTP code
 * @property phoneNumberOrEmail [PhoneNumberOrEmail] User's phone number
 * @property expiryTime [LocalDateTime] When the OTP code will expire
 */
data class OtpCode(
    val code: String,
    val phoneNumberOrEmail: PhoneNumberOrEmail,
    val expiryTime: LocalDateTime,
    val otpUsed: Boolean = false
)
