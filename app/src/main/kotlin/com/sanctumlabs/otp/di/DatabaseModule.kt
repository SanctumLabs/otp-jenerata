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
    val driver: String
    val url: String
    val driverClass: String
    val userName: String
    val password: String
}

class DatabaseConfigImpl : DatabaseConfig, KoinComponent {

    private val config: Config by inject()

    override val url = "jdbc:${config.getPropertyOrThrow("database.driver")}://" +
            config.getPropertyOrThrow("database.host") + ":" +
            config.getPropertyOrThrow("database.port") + "/" +
            config.getPropertyOrThrow("database.name")
    override val driverClass = config.getPropertyOrThrow("database.driverClass")
    override val driver = config.getPropertyOrThrow("database.driver")
    override val userName = config.getPropertyOrThrow("database.username")
    override val password = config.getPropertyOrThrow("database.password")
}

val databaseModule = module {
    val databaseConfig = DatabaseConfigImpl()
    DatabaseFactory.init(
        DatabaseParams(
            driver = databaseConfig.driver,
            url = databaseConfig.url,
            driverClass = databaseConfig.driverClass,
            username = databaseConfig.userName,
            password = databaseConfig.password
        )
    )
    single { OtpRepository }
    single<OtpDataStore> { OtpDatastoreImpl(get()) }
}
