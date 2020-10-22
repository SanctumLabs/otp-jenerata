package com.garihub.otp.database

import com.garihub.otp.core.MobilePhoneNumber
import com.garihub.otp.core.OtpCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserOtpRepository : JpaRepository<UserOtpEntity, Long> {
    fun findByOtpCodeAndPhoneNumber(otpCode: OtpCode, phoneNumber: MobilePhoneNumber): UserOtpEntity?

    fun findByOtpCodeAndPhoneNumberAndOtpUsedFalse(otpCode: OtpCode, phoneNumber: MobilePhoneNumber): UserOtpEntity?

    fun findByPhoneNumber(phoneNumber: MobilePhoneNumber): UserOtpEntity?
}
