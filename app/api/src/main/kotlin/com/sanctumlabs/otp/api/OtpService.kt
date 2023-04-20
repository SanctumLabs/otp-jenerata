package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpVerifyDto
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.services.CreateOtpService
import com.sanctumlabs.otp.core.services.VerifyOtpService

class OtpService(
    private val createOtpService: CreateOtpService,
    private val verifyOtpService: VerifyOtpService
) {

    /**
     * Verifies user OTP code
     * @return [OtpVerificationStatus]
     */
    suspend fun verifyOtp(verifyOtpDto: OtpVerifyDto): OtpVerificationStatus = run {
        verifyOtpService.execute(
            VerifyOtpCode(
                otpCode = verifyOtpDto.code, userId = UserId(verifyOtpDto.userId)
            )
        )
    }

    suspend fun generateOtp(otpRequestDto: OtpRequestDto): OtpCode = run {
        createOtpService.execute(UserId(otpRequestDto.userId))
    }
}
