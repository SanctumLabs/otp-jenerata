package com.sanctumlabs.otp.api.dto

import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import kotlinx.serialization.Serializable

@Serializable
data class OtpVerifyResponseDto(val userId: String, val code: String, val status: OtpVerificationStatus)
