package com.garihub.user.config.core

import com.garihub.user.core.gateways.auth.AuthServiceApi
import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.gateways.email.EmailGatewayApi
import com.garihub.user.core.usecases.RegisterRiderUseCase
import com.garihub.user.core.usecases.DeleteUserUseCase
import com.garihub.user.core.usecases.VerifyRiderEmailAddressUseCase
import com.garihub.user.core.usecases.GetUserUseCase
import com.garihub.user.core.usecases.SendEmailUseCase
import com.garihub.user.core.usecases.UpdateUserUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfig {

    @Bean
    fun getAllUsersUseCase(dataStore: DataStore, authService: AuthServiceApi): VerifyRiderEmailAddressUseCase {
        return VerifyRiderEmailAddressUseCase(dataStore, authService)
    }

    @Bean
    fun getAUserUseCase(dataStore: DataStore): GetUserUseCase {
        return GetUserUseCase(dataStore)
    }

    @Bean
    fun createUserUseCase(dataStore: DataStore, authService: AuthServiceApi): RegisterRiderUseCase {
        return RegisterRiderUseCase(dataStore, authService)
    }

    @Bean
    fun updateAnEventUseCase(dataStore: DataStore): UpdateUserUseCase {
        return UpdateUserUseCase(dataStore)
    }

    @Bean
    fun deleteAnEventUseCase(dataStore: DataStore): DeleteUserUseCase {
        return DeleteUserUseCase(dataStore)
    }

    @Bean
    fun sendEmailUseCase(emailGatewayApi: EmailGatewayApi): SendEmailUseCase {
        return SendEmailUseCase(emailGatewayApi)
    }
}
