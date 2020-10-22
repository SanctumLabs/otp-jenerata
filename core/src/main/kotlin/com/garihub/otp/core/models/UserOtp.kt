package com.garihub.otp.core.models

import com.garihub.otp.core.MobilePhoneNumber
import com.garihub.otp.core.OtpCode
import java.time.LocalDateTime

/**
 * Representation of a User OTP in the platform
 * @property otpCode [OtpCode] Generated OTP code
 * @property phoneNumber [MobilePhoneNumber] User's phone number
 * @property expiryTime [LocalDateTime] When the OTP code will expire
 */
data class UserOtp(
    val otpCode: OtpCode,
    val phoneNumber: MobilePhoneNumber,
    val expiryTime: LocalDateTime,
    val otpUsed: Boolean = false
)