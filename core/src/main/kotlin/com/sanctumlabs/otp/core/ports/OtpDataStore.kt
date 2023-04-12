package com.sanctumlabs.otp.core.ports

import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.OtpCode

interface OtpDataStore {
    /**
     * Find OTP code by the code by otp code & channel
     * @param code [OtpCode] passed in OTP code
     * @return [OtpCode] OTP entity
     */
    fun getOtpCode(code: String): OtpCode?

    /**
     * Finds an OtpCode by the provided channel if exists
     */
    fun getByUserId(userId: UserId): OtpCode?

    /**
     * Save generated OTP or updates an existing OTP
     */
    fun create(otpCode: OtpCode): OtpCode

    /**
     * Marks OTP code as used
     */
    fun markOtpAsUsed(otpCode: OtpCode)
}
