package com.sanctumlabs.otp.datastore.utils

import com.sanctumlabs.otp.datastore.DatabaseFactory
import com.sanctumlabs.otp.datastore.DatabaseParams
import org.jetbrains.exposed.sql.Database

/**
 * TestDatabase is a wrapper around a test database that is used to run integration tests.
 */
object TestDatabase {
    fun init(
        url: String = DATABASE_URL,
        username: String = DATABASE_USERNAME,
        password: String = DATABASE_PASSWORD,
        driver: String = DATABASE_DRIVER,
        driverClass: String = DATABASE_DRIVER_CLASS
    ): Database {
        return DatabaseFactory.init(
            DatabaseParams(
                driver = driver,
                url = url,
                driverClass = driverClass,
                username = username,
                password = password
            )
        )
    }
}
