package com.garihub.user.services.email.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param email
 * @param name
 */

data class FromDto(
    @JsonProperty("email")
    val email: String,

    @JsonProperty("name")
    val name: String? = null
)
