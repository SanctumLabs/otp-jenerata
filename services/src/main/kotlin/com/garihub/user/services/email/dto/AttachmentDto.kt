package com.garihub.user.services.email.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param content
 * @param filename
 */

data class AttachmentDto(
    @JsonProperty("content")
    val content: String,

    @JsonProperty("filename")
    val filename: String
)
