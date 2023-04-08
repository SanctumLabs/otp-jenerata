package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.OtpChannel
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.services.CreateOtpService
import com.sanctumlabs.otp.domain.exceptions.OtpGenerationException
import com.sanctumlabs.otp.domain.generators.TimeBasedCodeGenerator
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

private const val AMOUNT_TO_ADD = 180L

/**
 * Creates an OTP code for given user
 */
class CreateOtpServiceImpl(
    private val dataStore: OtpDataStore,
    private val generator: TimeBasedCodeGenerator
) : CreateOtpService {

    override fun execute(request: OtpChannel): OtpCode {
        return runCatching {
            val currentTime = Instant.now()
            val nextExpiry = currentTime.plus(AMOUNT_TO_ADD, ChronoUnit.SECONDS)
            val expiryTime = LocalDateTime.ofInstant(nextExpiry, ZoneOffset.UTC)

            val code = generator.generate(request.value)

            OtpCode(code = code, otpChannel = request, expiryTime = expiryTime)
        }
            .onSuccess(dataStore::create)
            .getOrElse {
                throw OtpGenerationException("Failed to generate OTP. Err: ${it.message}")
            }
    }
}
