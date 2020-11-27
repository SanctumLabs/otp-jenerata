package com.garihub.otp.database

import com.garihub.otp.core.PhoneNumberOrEmail
import com.garihub.otp.core.OtpCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserOtpRepository : JpaRepository<UserOtpEntity, Long> {

    @Query("SELECT u FROM userOtps u WHERE u.phoneNumberOrEmail = :phoneNumberOrEmail AND u.otpCode = :otpCode")
    fun findUserOtpByOtpCodeAndPhoneNumber(
        @Param("otpCode") otpCode: OtpCode,
        @Param("phoneNumberOrEmail") phoneNumberOrEmail: PhoneNumberOrEmail
    ): UserOtpEntity?

    @Query("SELECT u FROM userOtps u WHERE u.phoneNumberOrEmail = :phoneNumberOrEmail")
    fun findUserOtpByPhoneNumberOrEmail(
        @Param("phoneNumberOrEmail")
        phoneNumberOrEmail: PhoneNumberOrEmail
    ): UserOtpEntity?
}
