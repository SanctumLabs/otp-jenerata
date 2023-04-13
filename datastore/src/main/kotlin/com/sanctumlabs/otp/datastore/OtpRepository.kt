package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class OtpRepository {

    fun insert(otpCode: OtpCode): OtpEntity = run {
        transaction {
            OtpEntity.new {
                code = otpCode.code
                userId = otpCode.userId.value
                expiryTime = otpCode.expiryTime
            }
        }
    }

    fun findAll(): Collection<OtpEntity> = run {
        transaction { OtpEntity.all().toList() }
    }

    fun findAllByUserId(userId: UserId): Collection<OtpEntity> = run {
        transaction { OtpEntity.find { OtpTable.userId eq userId.value }.toList() }
    }

    fun findByCode(code: String): OtpEntity? {
        return OtpEntity.find {
            OtpTable.code eq code
        }
            .firstOrNull()
    }

    fun update(otpEntity: OtpEntity) = run {
        transaction {
            OtpTable.update({ OtpTable.code eq otpEntity.code }) {
                it[used] = otpEntity.used
                it[userId] = otpEntity.userId
                it[expiryTime] = otpEntity.expiryTime
            }
        }
    }
}
