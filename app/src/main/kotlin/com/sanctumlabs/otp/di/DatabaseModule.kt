package com.sanctumlabs.otp.di

import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.database.AppDatabase
import com.sanctumlabs.otp.database.DatabaseConfigImpl
import com.sanctumlabs.otp.datastore.OtpDatastoreImpl
import com.sanctumlabs.otp.datastore.OtpRepository
import org.koin.dsl.module

val databaseModule = module {
    val databaseConfig = DatabaseConfigImpl()
    val database = AppDatabase(databaseConfig).connect()
    single { database }
    single { OtpRepository }
    single<OtpDataStore> { OtpDatastoreImpl(get()) }
}
