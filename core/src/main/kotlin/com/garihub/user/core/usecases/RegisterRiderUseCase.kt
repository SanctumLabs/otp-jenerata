package com.garihub.user.core.usecases

import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.interactor.UseCase
import com.garihub.user.core.models.User
import com.garihub.user.core.enums.RegistrationStatus
import com.garihub.user.core.exceptions.AuthException
import com.garihub.user.core.exceptions.DBException
import com.garihub.user.core.exceptions.UserRegisteredException
import com.garihub.user.core.gateways.auth.AuthServiceApi

/**
 * Registers a user
 */
class RegisterRiderUseCase(
    private val dataStore: DataStore,
    private val authService: AuthServiceApi
) : UseCase<User, String>() {

    override fun execute(params: User?): String {
        requireNotNull(params) { "Must pass in a user" }

        // check if user is already registered
        val isRegistered = authService.isRiderUserRegistered(params.emailAddress)

        if (isRegistered) {
            throw UserRegisteredException("User already registered")
        } else {
            @Suppress("TooGenericExceptionCaught")
            return try {

                // trigger registration of user with Authorization server before creating this user in the datastore
                val response = authService.registerRiderUser(
                    firstName = params.firstName,
                    lastName = params.lastName,
                    email = params.emailAddress,
                    phoneNumber = params.phoneNumber,
                    password = params.password!!,
                    gender = params.gender
                )

                if (response) dataStore.createUser(params) else RegistrationStatus.FAILED.name
            } catch (e: Exception) {
                when (e) {
                    is AuthException -> RegistrationStatus.FAILED.name
                    is DBException -> RegistrationStatus.INTERNAL_ERROR.name
                    is UserRegisteredException -> RegistrationStatus.USER_EXISTS.name
                    else -> RegistrationStatus.FAILED.name
                }
            }
        }
    }
}
