package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import com.sanctumlabs.otp.core.ports.OtpDataStore

class OtpDatastoreImpl(private val otpRepository: OtpRepository) : OtpDataStore {

    override fun create(otpCode: OtpCode): OtpCode = runCatching {
        otpRepository.insert(otpCode)
        otpCode
    }
        .getOrElse { throw DatabaseException("Failed to create OTP code $otpCode", it) }

    override fun markOtpAsUsed(otpCode: OtpCode) {
        val otpEntity =
            otpRepository.findByCode(otpCode.code) ?: throw NotFoundException("Otp ${otpCode.code} not found")

        runCatching {
            otpEntity.used = otpCode.used
            otpRepository.update(otpEntity)
        }
            .getOrElse { throw DatabaseException("Failed to create OTP code $otpCode", it) }
    }

    override fun getOtpCode(code: String): OtpCode {
        val otpEntity =
            otpRepository.findByCode(code) ?: throw NotFoundException("Otp $code not found")

        return OtpCode(
            code = code,
            userId = UserId(otpEntity.userId),
            expiryTime = otpEntity.expiryTime,
            used = otpEntity.used
        )
    }

    override fun getByUserId(userId: UserId): OtpCode? {
        TODO("Not yet implemented")
    }
}
