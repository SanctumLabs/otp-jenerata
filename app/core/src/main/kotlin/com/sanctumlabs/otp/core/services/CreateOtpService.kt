package com.sanctumlabs.otp.core.services

import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.interactor.UseCase

/**
 * Generates an OTP code for given user
 */
interface CreateOtpService : UseCase<UserId, OtpCode> {
    override fun execute(request: UserId): OtpCode
}
