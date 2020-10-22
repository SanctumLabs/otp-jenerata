package com.garihub.otp.core.usecases

import com.garihub.otp.core.MobilePhoneNumber
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
) : UseCase<MobilePhoneNumber, OtpCode?>() {

    override fun execute(params: MobilePhoneNumber?): OtpCode? {
        requireNotNull(params) { "Must pass in a valid phone number" }

        val generatedOtp = generateOtp(otpKey, params)

        return try {
            val userOtp = UserOtp(
                phoneNumber = params,
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
