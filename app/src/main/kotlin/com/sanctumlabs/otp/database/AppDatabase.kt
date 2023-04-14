package com.sanctumlabs.otp.database

import org.jetbrains.exposed.sql.Database

class AppDatabase(private val config: DatabaseConfig) {
    fun connect(cleanDb: Boolean = false): Database {
        return Database.connect(
            createDataSource(
                dbUrl = config.url,
                username = config.userName,
                password = config.password,
                driverClassName = config.driverClass
            )
        )
            .apply {
                migrate(this, cleanDb)
            }
    }
}
