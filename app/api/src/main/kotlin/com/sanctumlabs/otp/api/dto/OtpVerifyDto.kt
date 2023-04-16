package com.sanctumlabs.otp.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class OtpVerifyDto(val userId: String, val code: String)
