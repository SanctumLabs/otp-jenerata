package com.sanctumlabs.otp.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class OtpResponseDto(
    val userId: String,
    val code: String
)
