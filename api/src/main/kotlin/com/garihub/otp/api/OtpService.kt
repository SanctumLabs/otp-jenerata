package com.garihub.otp.api

import com.garihub.otp.api.dto.UserOtpDto
import com.garihub.otp.api.dto.UserVerifyOtpDto
import com.garihub.otp.core.OtpCode
import com.garihub.otp.core.enums.OtpVerificationStatus
import com.garihub.otp.core.models.UserVerifyOtp
import com.garihub.otp.core.usecases.GenerateOtpUseCase
import com.garihub.otp.core.usecases.VerifyOtpUseCase
import com.garihub.otp.core.utils.isPhoneNumberValid
import org.springframework.stereotype.Service
import kotlin.Exception

@Service
class OtpService(
    private val generateOtpUseCase: GenerateOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
) {

    /**
     * Verifies user OTP code
     * @return [OtpVerificationStatus]
     */
    fun verifyOtp(userVerifyOtp: UserVerifyOtpDto): OtpVerificationStatus {
        @Suppress("TooGenericExceptionCaught")
        return try {
            verifyOtpUseCase.execute(
                UserVerifyOtp(otpCode = userVerifyOtp.otpCode, phoneNumberOrEmail = userVerifyOtp.phoneNumberOrEmail)
            )
        } catch (e: Exception) {
            OtpVerificationStatus.FAILED_VERIFICATION
        }
    }

    @Suppress("TooGenericExceptionCaught")
    fun generateOtp(userOtpDto: UserOtpDto): OtpCode? {
        return try {
            generateOtpUseCase.execute(userOtpDto.phoneNumber)
        } catch (e: Exception) {
            null
        }
    }
}
