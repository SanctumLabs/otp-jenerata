package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.datastore.DatabaseFactory.dbQuery
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object OtpRepository {

    suspend fun insert(otpCode: OtpCode): OtpEntity = dbQuery {
        OtpEntity.new {
            code = otpCode.code
            userId = otpCode.userId.value
            expiryTime = otpCode.expiryTime.toJavaLocalDateTime()
            used = otpCode.used
        }
    }

    suspend fun findAll(): Collection<OtpEntity> = dbQuery {
        transaction { OtpEntity.all().toList() }
    }

    suspend fun findAllByUserId(userId: String): Collection<OtpEntity> = dbQuery {
        transaction { OtpEntity.find { OtpTable.userId eq userId }.toList() }
    }

    suspend fun findByCode(code: String): OtpEntity? = dbQuery {
        OtpEntity.find {
            OtpTable.code eq code
        }
            .firstOrNull()
    }

    suspend fun update(otpEntity: OtpEntity) = dbQuery {
        OtpTable.update({ OtpTable.code eq otpEntity.code }) {
            it[used] = otpEntity.used
            it[userId] = otpEntity.userId
            it[expiryTime] = otpEntity.expiryTime
        }
    }

    suspend fun markAsUsed(code: String) = dbQuery {
        OtpTable.update({ OtpTable.code eq code }) {
            it[used] = true
        }
    }
}
