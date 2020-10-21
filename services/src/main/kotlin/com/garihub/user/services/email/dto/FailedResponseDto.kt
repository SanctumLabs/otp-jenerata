package com.garihub.user.services.email.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param errors Array of error messages
 */

data class FailedResponseDto(
    /* Array of error messages */
    @JsonProperty("errors")
    val errors: ArrayList<FailedResponseErrorsDto>? = null
)
