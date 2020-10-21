package com.garihub.user.config.core

import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.database.DataStoreImpl
import com.garihub.user.database.user.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataStoreConfig {

    @Bean
    fun dataStore(userRepository: UserRepository): DataStore {
        return DataStoreImpl(userRepository)
    }
}
