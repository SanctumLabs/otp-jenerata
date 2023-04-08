package com.sanctumlabs.otp.core.entities

import java.time.LocalDateTime

/**
 * Representation of a User OTP in the platform
 * @property code [String] Generated OTP code
 * @property otpChannel [OtpChannel] Channel in which this OTP code will be sent
 * @property expiryTime [LocalDateTime] When the OTP code will expire
 */
data class OtpCode(
    val code: String,
    val otpChannel: OtpChannel,
    val expiryTime: LocalDateTime,
    val otpUsed: Boolean = false
)
