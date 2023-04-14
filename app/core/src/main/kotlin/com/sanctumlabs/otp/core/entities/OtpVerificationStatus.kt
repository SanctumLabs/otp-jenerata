package com.sanctumlabs.otp.core.entities

enum class OtpVerificationStatus {
    VERIFIED,
    FAILED_VERIFICATION,
    USER_NOT_FOUND,
    CODE_EXPIRED
}
