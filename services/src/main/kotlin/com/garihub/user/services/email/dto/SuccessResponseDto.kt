package com.garihub.user.services.email.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param message
 */

data class SuccessResponseDto(
    @JsonProperty("message")
    val message: String? = null
)
