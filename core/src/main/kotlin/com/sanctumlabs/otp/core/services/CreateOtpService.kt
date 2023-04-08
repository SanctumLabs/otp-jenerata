package com.sanctumlabs.otp.core.services

import com.sanctumlabs.otp.core.entities.OtpChannel
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.interactor.UseCase

/**
 * Generates an OTP code for given user
 */
interface CreateOtpService : UseCase<OtpChannel, OtpCode> {
    override fun execute(request: OtpChannel): OtpCode
}
