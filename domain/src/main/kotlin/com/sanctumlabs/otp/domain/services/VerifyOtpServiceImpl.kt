package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.services.VerifyOtpService
import com.sanctumlabs.otp.domain.exceptions.NotFoundException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


class VerifyOtpServiceImpl(private val dataStore: OtpDataStore) : VerifyOtpService {

    override fun execute(request: VerifyOtpCode): OtpVerificationStatus {
        val otpCode = dataStore.getOtpCode(request.otpCode)
            ?: throw NotFoundException("User OTP Not found")

        return runCatching {
            val instant: Instant = Instant.now()
            val now = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
            val expiryDate = otpCode.expiryTime
            val isOtpValid = now.isBefore(expiryDate)

            if (isOtpValid) {
                dataStore.markOtpAsUsed(otpCode)
                OtpVerificationStatus.VERIFIED
            } else {
                OtpVerificationStatus.CODE_EXPIRED
            }
        }
            .getOrElse {
                OtpVerificationStatus.FAILED_VERIFICATION
            }
    }
}
