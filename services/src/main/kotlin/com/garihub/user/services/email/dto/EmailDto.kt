package com.garihub.user.services.email.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Email Object
 * @param to
 * @param subject
 * @param message
 */

data class EmailDto(
    @JsonProperty("to")
    val to: ArrayList<String>,

    @JsonProperty("subject")
    val subject: String,

    @JsonProperty("message")
    val message: String
)
