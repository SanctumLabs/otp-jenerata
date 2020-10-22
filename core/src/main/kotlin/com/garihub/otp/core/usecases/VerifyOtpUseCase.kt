package com.garihub.otp.core.usecases

import com.garihub.otp.core.enums.OtpVerificationStatus
import com.garihub.otp.core.exceptions.NotFoundException
import com.garihub.otp.core.gateways.datastore.DataStore
import com.garihub.otp.core.interactor.UseCase
import com.garihub.otp.core.models.UserVerifyOtp
import com.garihub.otp.core.utils.verifyOtp


class VerifyOtpUseCase(private val dataStore: DataStore) : UseCase<UserVerifyOtp, OtpVerificationStatus>() {

    override fun execute(params: UserVerifyOtp?): OtpVerificationStatus {
        requireNotNull(params) { "OTP can not be null" }

        val userOtp = dataStore.getByOtpCodeAndPhoneNumber(params.otpCode, params.phoneNumber)
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
