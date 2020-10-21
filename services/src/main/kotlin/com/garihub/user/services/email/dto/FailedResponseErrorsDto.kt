package com.garihub.user.services.email.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param errors
 */

data class FailedResponseErrorsDto(
    @JsonProperty("errors")
    val errors: Any? = null
)
