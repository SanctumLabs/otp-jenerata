package com.garihub.user.services.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

// Ref https://www.keycloak.org/docs-api/11.0/rest-api/index.html#_userrepresentation
data class UserUpdateDto(
    @JsonProperty("emailVerified")
    val emailVerified: Boolean = false,

    @JsonProperty("enabled")
    val enabled: Boolean = false
)
