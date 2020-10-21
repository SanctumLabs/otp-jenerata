package com.garihub.user.services.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

// Ref https://www.keycloak.org/docs-api/11.0/rest-api/index.html#_userrepresentation
data class UserRepresentationDto(
    @JsonProperty("id")
    val id: String? = null,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("emailVerified")
    val emailVerified: Boolean,

    @JsonProperty("enabled")
    val enabled: Boolean,

    @JsonProperty("firstName")
    val firstName: String,

    @JsonProperty("lastName")
    val lastName: String,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("attributes")
    val attributes: HashMap<String, Any>,

    @JsonProperty("credentials", required = false)
    val credentials: ArrayList<CredentialsRepresentationDto> = arrayListOf()
)
