package com.sanctumlabs.otp.database

import com.sanctumlabs.otp.config.Config
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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
