package com.sanctumlabs.otp.di

import com.sanctumlabs.otp.config.Config
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.datastore.DatabaseFactory
import com.sanctumlabs.otp.datastore.DatabaseParams
import com.sanctumlabs.otp.datastore.OtpDatastoreImpl
import com.sanctumlabs.otp.datastore.OtpRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

interface DatabaseConfig {
    val host: String
    val port: Int
    val name: String
    val username: String
    val password: String
    val driver: String
    val driverClass: String
    val url: String
}

class DatabaseConfigImpl : DatabaseConfig, KoinComponent {

    private val config: Config by inject()
    override val host: String
        get() = config.getPropertyOrThrow("database.host")
    override val port: Int
        get() = config.getPropertyOrThrow("database.port").toInt()
    override val name: String
        get() = config.getPropertyOrThrow("database.name")

    override val username: String = config.getPropertyOrThrow("database.username")
    override val password: String = config.getPropertyOrThrow("database.password")
    override val driver: String = config.getPropertyOrThrow("database.driver")
    override val driverClass: String = config.getPropertyOrThrow("database.driverClass")

    override val url = config.getProperty(
        "database.url", "jdbc:${config.getPropertyOrThrow("database.driver")}://" +
                config.getPropertyOrThrow("database.host") + ":" +
                config.getPropertyOrThrow("database.port") + "/" +
                config.getPropertyOrThrow("database.name")
    )
}

val databaseModule = module {
    val databaseConfig = DatabaseConfigImpl()
    DatabaseFactory.init(
        DatabaseParams(
            driver = databaseConfig.driver,
            url = databaseConfig.url,
            driverClass = databaseConfig.driverClass,
            username = databaseConfig.username,
            password = databaseConfig.password
        )
    )
    single { OtpRepository }
    single<OtpDataStore> { OtpDatastoreImpl(get()) }
}
