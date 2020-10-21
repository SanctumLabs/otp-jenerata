package com.garihub.user.services.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

// Ref https://www.keycloak.org/docs-api/11.0/rest-api/index.html#_credentialrepresentation
data class CredentialsRepresentationDto(
    @JsonProperty("credentialData")
    val credentialData: String,

    @JsonProperty("type")
    val type: String,

    @JsonProperty("value")
    val value: String
)
