package com.sanctumlabs.otp.domain

import com.sanctumlabs.otp.datastore.DatabaseFactory
import com.sanctumlabs.otp.datastore.DatabaseParams
import com.sanctumlabs.otp.datastore.models.OtpTable
import com.sanctumlabs.otp.testfixtures.extensions.DatabaseExtension
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_DRIVER
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_DRIVER_CLASS
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_PASSWORD
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_URL
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_USERNAME
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(DatabaseExtension::class)
open class BaseIntegrationTest {
    private lateinit var database: Database

    @BeforeEach
    open fun setup() {
        database = DatabaseFactory.init(
            DatabaseParams(
                driver = DATABASE_DRIVER,
                url = DATABASE_URL,
                driverClass = DATABASE_DRIVER_CLASS,
                username = DATABASE_USERNAME,
                password = DATABASE_PASSWORD
            )
        )
    }

    @AfterEach
    open fun teardown() {
        transaction(database) {
            SchemaUtils.drop(OtpTable)
        }
    }
}
