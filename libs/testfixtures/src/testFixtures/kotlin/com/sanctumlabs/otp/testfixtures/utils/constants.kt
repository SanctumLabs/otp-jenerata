package com.sanctumlabs.otp.testfixtures.utils

const val POSTGRESQL_VERSION = "postgres:15"
const val POSTGRESQL_DATABASE_PORT = 5432

const val DATABASE_DRIVER = "postgresql"
const val DATABASE_DRIVER_CLASS = "org.testcontainers.jdbc.ContainerDatabaseDriver"
const val DATABASE_USERNAME = "otp-user"
const val DATABASE_PASSWORD = "otp-password"
const val DATABASE_NAME = "otpdb"
const val DATABASE_URL = "jdbc:tc:postgresql:///$DATABASE_NAME"
