package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.datastore.models.OtpEntity

fun mapModelToEntity(otpEntity: OtpEntity): OtpCode {
    return OtpCode(
        code = otpEntity.code,
        userId = UserId(otpEntity.userId),
        expiryTime = otpEntity.expiryTime,
        used = otpEntity.used
    )
}
