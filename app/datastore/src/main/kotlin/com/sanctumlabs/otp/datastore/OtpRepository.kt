package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object OtpRepository {

    fun insert(otpCode: OtpCode): OtpEntity = run {
        transaction {
            OtpEntity.new {
                code = otpCode.code
                userId = otpCode.userId.value
                expiryTime = otpCode.expiryTime.toJavaLocalDateTime()
            }
        }
    }

    fun findAll(): Collection<OtpEntity> = run {
        transaction { OtpEntity.all().toList() }
    }

    fun findAllByUserId(userId: String): Collection<OtpEntity> = run {
        transaction { OtpEntity.find { OtpTable.userId eq userId }.toList() }
    }

    fun findByCode(code: String): OtpEntity? {
        return OtpEntity.find {
            OtpTable.code eq code
        }
            .firstOrNull()
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

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
