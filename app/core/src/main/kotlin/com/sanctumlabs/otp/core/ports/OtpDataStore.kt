package com.sanctumlabs.otp.core.ports

import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.OtpCode

interface OtpDataStore {
    /**
     * Save generated OTP or updates an existing OTP
     */
    suspend fun create(otpCode: OtpCode): OtpCode

    /**
     * Find OTP code by the code by otp code & channel
     * @param code [OtpCode] passed in OTP code
     * @return [OtpCode] OTP entity
     */
    suspend fun getOtpCode(code: String): OtpCode

    /**
     * Finds all OtpCode(s)
     */

    suspend fun getAllByUserId(userId: UserId): Collection<OtpCode>

    suspend fun getAll(): Collection<OtpCode>

    /**
     * Marks OTP code as used
     */
    suspend fun markOtpAsUsed(otpCode: OtpCode)
}
