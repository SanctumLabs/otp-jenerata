package com.garihub.otp.core.models

import com.garihub.otp.core.PhoneNumberOrEmail
import com.garihub.otp.core.OtpCode
import java.time.LocalDateTime

/**
 * Representation of a User OTP in the platform
 * @property otpCode [OtpCode] Generated OTP code
 * @property phoneNumberOrEmail [PhoneNumberOrEmail] User's phone number
 * @property expiryTime [LocalDateTime] When the OTP code will expire
 */
data class UserOtp(
    val otpCode: OtpCode,
    val phoneNumberOrEmail: PhoneNumberOrEmail,
    val expiryTime: LocalDateTime,
    val otpUsed: Boolean = false
)
