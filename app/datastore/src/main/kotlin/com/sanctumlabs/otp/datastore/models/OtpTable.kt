package com.sanctumlabs.otp.datastore.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object OtpTable : IntIdTable(name = "otps") {
    val code: Column<String> = varchar("code", length = 10).uniqueIndex()
    val userId: Column<String> = varchar(name = "user_id", length = 255)
    val expiryTime: Column<LocalDateTime> = datetime(name = "expiry_time")
    val createdOn: Column<LocalDateTime> = datetime(name = "created_on").default(LocalDateTime.now())
    val updatedOn: Column<LocalDateTime> = datetime(name = "updated_on").default(LocalDateTime.now())
    val used: Column<Boolean> = bool(name = "used").default(false)
}
