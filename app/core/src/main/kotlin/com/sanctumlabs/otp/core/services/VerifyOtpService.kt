package com.sanctumlabs.otp.core.services

import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.interactor.UseCase

interface VerifyOtpService : UseCase<VerifyOtpCode, OtpVerificationStatus> {
    override suspend fun execute(request: VerifyOtpCode): OtpVerificationStatus
}
