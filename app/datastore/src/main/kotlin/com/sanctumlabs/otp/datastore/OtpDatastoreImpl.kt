package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import com.sanctumlabs.otp.core.ports.OtpDataStore

class OtpDatastoreImpl(private val otpRepository: OtpRepository) : OtpDataStore {

    override suspend fun create(otpCode: OtpCode): OtpCode = runCatching {
        otpRepository.insert(otpCode)
        otpCode
    }
        .getOrElse { throw DatabaseException("Failed to create OTP code $otpCode", it) }

    override suspend fun markOtpAsUsed(otpCode: OtpCode) {
        otpRepository.findByCode(otpCode.code) ?: throw NotFoundException("Otp ${otpCode.code} not found")

        runCatching { otpRepository.markAsUsed(otpCode.code) }
            .getOrElse { throw DatabaseException("Failed to mark OTP $otpCode as used", it) }
    }

    override suspend fun getOtpCode(code: String): OtpCode {
        val otpEntity = otpRepository.findByCode(code) ?: throw NotFoundException("Otp $code not found")
        return mapModelToEntity(otpEntity)
    }

    override suspend fun getAll(): Collection<OtpCode> = runCatching { otpRepository.findAll() }
        .mapCatching { it.map(::mapModelToEntity) }
        .getOrElse { throw DatabaseException("Failed to get all OTP codes", it) }

    override suspend fun getAllByUserId(userId: UserId): Collection<OtpCode> = runCatching {
        otpRepository.findAllByUserId(userId.value)
    }
        .mapCatching { it.map(::mapModelToEntity) }
        .getOrElse { throw DatabaseException("Failed to get all OTP codes", it) }
}
