package com.sanctumlabs.otp.di

import org.koin.dsl.module

val apiModule = module {
    single<ExpensesService> { ExpensesServiceImpl(get(), get(), get(), get(), get()) }
}
