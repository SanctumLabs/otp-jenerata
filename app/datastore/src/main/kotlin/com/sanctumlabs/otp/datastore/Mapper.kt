package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.datastore.models.OtpEntity
import kotlinx.datetime.toKotlinLocalDateTime

fun mapModelToEntity(otpEntity: OtpEntity): OtpCode {
    return OtpCode(
        code = otpEntity.code,
        userId = UserId(otpEntity.userId),
        expiryTime = otpEntity.expiryTime.toKotlinLocalDateTime(),
        used = otpEntity.used
    )
}
