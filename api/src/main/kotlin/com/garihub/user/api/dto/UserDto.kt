package com.garihub.user.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.garihub.user.core.models.Gender
import com.garihub.user.core.models.UserType
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class UserDto(
    @JsonProperty("identifier", required = false)
    var identifier: String? = null,

    @NotNull(message = "First Name cannot be null")
    @JsonProperty("firstName")
    val firstName: String,

    @NotNull(message = "Last Name cannot be null")
    @JsonProperty("lastName")
    val lastName: String,

    @JsonProperty("gender")
    val gender: Gender,

    @JsonProperty("phoneNumber", required = true)
    val phoneNumber: String,

    @Email(message = "Email should be valid")
    @JsonProperty("emailAddress", required = true)
    val emailAddress: String,

    @JsonProperty("userType")
    val userType: UserType,

    @JsonProperty("password")
    var password: String? = null
)
