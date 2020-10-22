package com.garihub.otp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserOtpDto(
    @JsonProperty("phone_number", required = true)
    val phoneNumber: String
)
