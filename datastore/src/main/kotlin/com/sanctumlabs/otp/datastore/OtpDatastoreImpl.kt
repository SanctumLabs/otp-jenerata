package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import com.sanctumlabs.otp.core.ports.OtpDataStore

class OtpDatastoreImpl(private val otpRepository: OtpRepository) : OtpDataStore {

    override fun create(otpCode: OtpCode): OtpCode = runCatching {
        otpRepository.insert(otpCode)
        otpCode
    }
        .getOrElse { throw DatabaseException("Failed to create OTP code $otpCode", it) }

    override fun getOtpCode(code: String): OtpCode? {
        TODO("Not yet implemented")
    }

    override fun getByUserId(userId: UserId): OtpCode? {
        TODO("Not yet implemented")
    }

    override fun markOtpAsUsed(otpCode: OtpCode) {
        TODO("Not yet implemented")
    }
}
