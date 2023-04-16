package com.sanctumlabs.otp.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequestDto(val userId: String)
