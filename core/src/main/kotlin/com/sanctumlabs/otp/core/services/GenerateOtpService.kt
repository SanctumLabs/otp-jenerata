package com.sanctumlabs.otp.core.services

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.PhoneNumberOrEmail
import com.sanctumlabs.otp.core.exceptions.OtpGenerationException
import com.sanctumlabs.otp.core.interactor.UseCase
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.utils.generateOtp

/**
 * Generates an OTP code for given user
 */
class GenerateOtpUseCase(
    private val dataStore: OtpDataStore,
    private val otpKey: String
) : UseCase<PhoneNumberOrEmail, OtpCode?>() {

    override fun execute(params: PhoneNumberOrEmail?): OtpCode {
        requireNotNull(params) { "Must pass in a valid phone number" }

        val generatedOtp = generateOtp(otpKey, params)

        @Suppress("TooGenericExceptionCaught")
        return try {
            val otpCode = OtpCode(
                phoneNumberOrEmail = params,
                code = generatedOtp.code,
                expiryTime = generatedOtp.expiryTime
            )

            val response = dataStore.saveOtpCode(otpCode)

            if (response) {
                generatedOtp.code
            } else {
                null
            }
        } catch (e: Exception) {
            throw OtpGenerationException("Failed to generate OTP. Err: ${e.message}")
        }
    }
}
