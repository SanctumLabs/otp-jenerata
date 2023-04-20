package com.sanctumlabs.otp.api.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class OtpResponseDto(
    val userId: String,
    val code: String,
    val expiryTime: LocalDateTime
)
