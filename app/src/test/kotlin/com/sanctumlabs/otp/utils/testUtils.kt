package com.sanctumlabs.otp.utils

import io.ktor.server.config.*
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_DRIVER
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_DRIVER_CLASS
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_NAME
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_PASSWORD
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_URL
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_USERNAME
import com.sanctumlabs.otp.testfixtures.utils.POSTGRESQL_DATABASE_PORT

internal val testAppConfig = MapApplicationConfig(
    "ktor.environment" to "test",
    "database.host" to "",
    "database.port" to "$POSTGRESQL_DATABASE_PORT",
    "database.name" to DATABASE_NAME,
    "database.username" to DATABASE_USERNAME,
    "database.password" to DATABASE_PASSWORD,
    "database.driver" to DATABASE_DRIVER,
    "database.driver_class" to DATABASE_DRIVER_CLASS,
    "database.url" to DATABASE_URL,
    "jwt.domain" to "https://jwt-provider-domain/",
    "jwt.audience" to "jwt-audience",
    "jwt.realm" to "otp",
    "jwt.secret" to "secret"
)
