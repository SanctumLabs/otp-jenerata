package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.services.VerifyOtpService
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime


class VerifyOtpServiceImpl(private val dataStore: OtpDataStore) : VerifyOtpService {

    override fun execute(request: VerifyOtpCode): OtpVerificationStatus {

        return runCatching {
            val otpCode = dataStore.getOtpCode(request.otpCode)
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            val expiryDate = otpCode.expiryTime.toJavaLocalDateTime()
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
                when (it) {
                    is NotFoundException -> throw it
                    else -> OtpVerificationStatus.FAILED_VERIFICATION
                }
            }
    }
}
