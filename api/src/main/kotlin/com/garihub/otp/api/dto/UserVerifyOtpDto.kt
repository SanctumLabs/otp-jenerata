package com.garihub.otp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.garihub.otp.core.MobilePhoneNumber
import com.garihub.otp.core.OtpCode

data class UserVerifyOtpDto(
    @JsonProperty("phone_number", required = true)
    val phoneNumber: MobilePhoneNumber,

    @JsonProperty("otp_code", required = true)
    val otpCode: OtpCode
)
