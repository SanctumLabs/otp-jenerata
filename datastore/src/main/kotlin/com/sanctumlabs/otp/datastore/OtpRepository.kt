package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import org.jetbrains.exposed.sql.transactions.transaction

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

    fun findByCode(code: String): OtpEntity? {
        return OtpEntity.find {
            OtpTable.code eq code
        }
            .firstOrNull()
    }

    fun getByChannel(userId: UserId): OtpCode? {
        TODO("Not yet implemented")
    }

    fun markOtpAsUsed(otpCode: OtpCode) {
        TODO("Not yet implemented")
    }
}
