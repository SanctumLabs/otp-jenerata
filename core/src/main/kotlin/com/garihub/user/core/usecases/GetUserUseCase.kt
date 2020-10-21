package com.garihub.user.core.usecases

import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.interactor.UseCase
import com.garihub.user.core.models.User
import com.garihub.user.core.UserIdentifier
import com.garihub.user.core.exceptions.UserException

class GetUserUseCase(private val dataStore: DataStore) :
    UseCase<UserIdentifier, User?>() {
    override fun execute(params: UserIdentifier?): User? {
        requireNotNull(params) { "Must have an identifier to get an event item" }
        return try {
            dataStore.getItem(params)
        } catch (e: UserException) {
            null
        }
    }
}
