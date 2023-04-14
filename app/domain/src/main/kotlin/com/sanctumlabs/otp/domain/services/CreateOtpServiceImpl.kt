package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.services.CreateOtpService
import com.sanctumlabs.otp.core.services.OtpCodeGenerator


/**
 * Creates an OTP code for given user
 */
class CreateOtpServiceImpl(
    private val dataStore: OtpDataStore,
    private val otpGenerator: OtpCodeGenerator,
) : CreateOtpService {

    override fun execute(request: UserId): OtpCode {
        return runCatching {
            val generatedCode = otpGenerator.generate(request.value)
            val (code, expiryTime) = generatedCode

            OtpCode(code = code, userId = request, expiryTime = expiryTime)
        }
            .onSuccess(dataStore::create)
            .getOrElse {
                throw CreateOtpException("Failed to create OTP for $request", it)
            }
    }
}
