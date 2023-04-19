package com.sanctumlabs.otp.api.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ApiResult(
    val message: String,
    @Contextual
    val data: Any? = null
)
