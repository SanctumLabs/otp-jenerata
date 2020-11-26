package com.garihub.otp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.garihub.otp.core.PhoneNumberOrEmail
import com.garihub.otp.core.OtpCode

data class UserOtpResponseDto(
    @JsonProperty("phone_number", required = true)
    val phoneNumberOrEmail: PhoneNumberOrEmail,

    @JsonProperty("otp_code", required = true)
    val otpCode: OtpCode
)
