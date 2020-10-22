package com.garihub.otp.core.models

import com.garihub.otp.core.MobilePhoneNumber
import com.garihub.otp.core.OtpCode

/**
 * Representation of a User Verification OTP
 * @property otpCode [OtpCode] Passed in OTP code
 * @property phoneNumber [MobilePhoneNumber] User's phone number
 */
data class UserVerifyOtp(
    val otpCode: OtpCode,
    val phoneNumber: MobilePhoneNumber
)
