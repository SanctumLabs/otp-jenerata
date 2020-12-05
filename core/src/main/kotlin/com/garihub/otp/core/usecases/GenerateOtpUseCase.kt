package com.garihub.otp.core.usecases

import com.garihub.otp.core.PhoneNumberOrEmail
import com.garihub.otp.core.OtpCode
import com.garihub.otp.core.exceptions.OtpGenerationException
import com.garihub.otp.core.gateways.datastore.DataStore
import com.garihub.otp.core.interactor.UseCase
import com.garihub.otp.core.models.UserOtp
import com.garihub.otp.core.utils.generateOtp

/**
 * Generates an OTP code for given user
 */
class GenerateOtpUseCase(
    private val dataStore: DataStore,
    private val otpKey: String
) : UseCase<PhoneNumberOrEmail, OtpCode?>() {

    override fun execute(params: PhoneNumberOrEmail?): OtpCode? {
        requireNotNull(params) { "Must pass in a valid phone number" }

        val generatedOtp = generateOtp(otpKey, params)

        @Suppress("TooGenericExceptionCaught")
        return try {
            val userOtp = UserOtp(
                phoneNumberOrEmail = params,
                otpCode = generatedOtp.otpCode,
                expiryTime = generatedOtp.expiryTime
            )

            val response = dataStore.saveOtpCode(userOtp)

            if (response) {
                generatedOtp.otpCode
            } else {
                null
            }
        } catch (e: Exception) {
            throw OtpGenerationException("Failed to generate OTP. Err: ${e.message}")
        }
    }
}
