package com.garihub.user.core.usecases

import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.interactor.UseCase
import com.garihub.user.core.UserIdentifier
import com.garihub.user.core.exceptions.UserException

class DeleteUserUseCase(private val dataStore: DataStore) : UseCase<UserIdentifier, Boolean>() {

    override fun execute(params: UserIdentifier?): Boolean {
        requireNotNull(params) { "Must pass in valid identifier" }
        return try {
            dataStore.deregister(params)
        } catch (e: UserException) {
            false
        }
    }
}
