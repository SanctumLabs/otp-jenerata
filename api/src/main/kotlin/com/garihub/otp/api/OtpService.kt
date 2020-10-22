package com.garihub.otp.api

import com.garihub.otp.api.dto.UserOtpDto
import com.garihub.otp.api.dto.UserVerifyOtpDto
import com.garihub.otp.core.OtpCode
import com.garihub.otp.core.enums.OtpVerificationStatus
import com.garihub.otp.core.exceptions.InvalidPhoneNumberException
import com.garihub.otp.core.models.UserVerifyOtp
import com.garihub.otp.core.usecases.GenerateOtpUseCase
import com.garihub.otp.core.usecases.VerifyOtpUseCase
import com.garihub.otp.core.utils.isPhoneNumberValid
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class OtpService(
    private val generateOtpUseCase: GenerateOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
) {

    /**
     * Verifies a user account
     * @return [Boolean] True if the account was successfully verified
     */
    fun verifyOtp(userVerifyOtp: UserVerifyOtpDto): OtpVerificationStatus {
        return verifyOtpUseCase.execute(
            UserVerifyOtp(
                otpCode = userVerifyOtp.otpCode,
                phoneNumber = userVerifyOtp.phoneNumber
            )
        )
    }

    @Suppress("TooGenericExceptionCaught")
    fun generateOtp(userOtpDto: UserOtpDto): OtpCode? {
        return try {
            if (isPhoneNumberValid(userOtpDto.phoneNumber)) {
                generateOtpUseCase.execute(userOtpDto.phoneNumber)
            } else {
                null
            }
        } catch (e: Exception) {
            @Suppress("ForbiddenComment")
            // todo: put in proper responses per exception thrown
            when (e) {
                is InvalidPhoneNumberException -> {
                    null
                }
                else -> null
            }
        }
    }
}
