package com.sanctumlabs.otp.domain.services

class CreateOtpException(message: String) : Exception(message) {
    constructor(message: String, cause: Throwable? = null) : this(message) {
        initCause(cause)
    }
}
