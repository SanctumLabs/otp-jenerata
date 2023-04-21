package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.datastore.models.OtpTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

object DatabaseFactory {
    fun init(config: DatabaseParams): Database {
        val database = Database.connect(
            createDataSource(
                dbUrl = config.url,
                user = config.username,
                dbPassword = config.password,
                driverName = config.driverClass
            )
        )

        transaction(database) {
            if (config.cleanDB) {
                SchemaUtils.drop(OtpTable)
                SchemaUtils.create(OtpTable)
            } else {
                SchemaUtils.create(OtpTable)
            }
        }

        return database
    }

    private fun createDataSource(
        dbUrl: String,
        user: String,
        dbPassword: String,
        driverName: String
    ): DataSource {
        return HikariDataSource(HikariConfig()
            .apply {
                jdbcUrl = dbUrl
                username = user
                password = dbPassword
                driverClassName = driverName
                isAutoCommit = true
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                addDataSourceProperty("cachePrepStmts", "true")
                addDataSourceProperty("prepStmtCacheSize", "250")
                addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
                validate()
            })
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
