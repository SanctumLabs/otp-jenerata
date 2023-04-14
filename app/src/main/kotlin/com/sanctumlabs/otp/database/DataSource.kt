package com.sanctumlabs.otp.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource


fun createDataSource(dbUrl: String, username: String, password: String, driverClassName: String): DataSource {
    val config = HikariConfig().apply {
        this.jdbcUrl = dbUrl
        this.username = username
        this.password = password
        this.driverClassName = driverClassName
        isAutoCommit = true
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    }

    config.validate()

    return HikariDataSource(config)
}
