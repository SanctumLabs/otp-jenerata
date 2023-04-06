package com.sanctumlabs.otp.core.services

import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import com.sanctumlabs.otp.core.interactor.UseCase
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.utils.verifyOtp


class VerifyOtpUseCase(private val dataStore: OtpDataStore) : UseCase<VerifyOtpCode, OtpVerificationStatus>() {

    override fun execute(params: VerifyOtpCode?): OtpVerificationStatus {
        requireNotNull(params) { "OTP can not be null" }

        val userOtp = dataStore.getByOtpCodeAndPhoneNumber(params.otpCode, params.phoneNumberOrEmail)
            ?: throw NotFoundException("User OTP Not found")

        @Suppress("TooGenericExceptionCaught")
        return try {
            val isOtpValid = verifyOtp(userOtp)

            if (isOtpValid) {
                dataStore.markOtpAsUsed(userOtp)
                OtpVerificationStatus.VERIFIED
            } else {
                OtpVerificationStatus.CODE_EXPIRED
            }
        } catch (e: Exception) {
            OtpVerificationStatus.FAILED_VERIFICATION
        }
    }
}
