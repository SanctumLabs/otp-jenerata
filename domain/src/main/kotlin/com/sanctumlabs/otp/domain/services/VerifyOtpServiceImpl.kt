package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.services.VerifyOtpService
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import java.time.LocalDateTime


class VerifyOtpServiceImpl(private val dataStore: OtpDataStore) : VerifyOtpService {

    override fun execute(request: VerifyOtpCode): OtpVerificationStatus {
        val otpCode = dataStore.getOtpCode(request.otpCode)
            ?: throw NotFoundException("User OTP Not found")

        return runCatching {
            val now = LocalDateTime.now()
            val expiryDate = otpCode.expiryTime
            val isOtpValid = now.isBefore(expiryDate)

            if (isOtpValid) {
                val usedOtpCode = otpCode.copy(used = true)
                dataStore.markOtpAsUsed(usedOtpCode)
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
