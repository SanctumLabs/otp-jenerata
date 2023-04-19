package com.sanctumlabs.otp.api.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class OtpResponseDto(
    val userId: String,
    val code: String,
    @Contextual
    val expiryTime: LocalDateTime
)
