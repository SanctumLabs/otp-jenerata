package com.garihub.otp.config.core

import com.garihub.otp.core.gateways.datastore.DataStore
import com.garihub.otp.database.DataStoreImpl
import com.garihub.otp.database.UserOtpRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataStoreConfig {

    @Bean
    fun dataStore(userOtpRepository: UserOtpRepository): DataStore {
        return DataStoreImpl(userOtpRepository)
    }
}
