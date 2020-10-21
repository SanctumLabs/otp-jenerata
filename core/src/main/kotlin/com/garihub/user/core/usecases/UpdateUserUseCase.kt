package com.garihub.user.core.usecases

import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.interactor.UseCase
import com.garihub.user.core.models.User
import com.garihub.user.core.exceptions.UserException

class UpdateUserUseCase(private val dataStore: DataStore) : UseCase<UpdateUserUseCase.Params, Boolean>() {

    override fun execute(params: Params?): Boolean {
        requireNotNull(params) { "Must pass in valid params to update an event" }
        return try {
            dataStore.update(identifier = params.identifier, user = params.user)
        } catch (e: UserException) {
            false
        }
    }

    data class Params(
        val identifier: String,
        val user: User
    )
}
