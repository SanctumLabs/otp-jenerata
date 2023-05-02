package com.sanctumlabs.otp.core.entities

import kotlinx.datetime.LocalDateTime

/**
 * Representation of a User OTP in the platform
 * @property code [String] Generated OTP code
 * @property userId [UserId] Channel in which this OTP code will be sent
 * @property expiryTime [LocalDateTime] When the OTP code will expire
 */
data class OtpCode(
    val code: String,
    val userId: UserId,
    val expiryTime: LocalDateTime,
    val used: Boolean = false
)
