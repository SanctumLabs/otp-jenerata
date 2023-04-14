package com.sanctumlabs.otp.database

import com.sanctumlabs.otp.datastore.models.OtpTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun migrate(database: Database, cleanDb: Boolean) {
    transaction(database) {
        if (cleanDb) {
            SchemaUtils.drop()
            SchemaUtils.create(OtpTable)
        } else {
            SchemaUtils.create(OtpTable)
        }
    }
}
