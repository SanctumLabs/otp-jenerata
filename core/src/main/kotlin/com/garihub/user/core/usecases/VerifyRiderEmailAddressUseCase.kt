package com.garihub.user.core.usecases

import com.garihub.user.core.VerificationToken
import com.garihub.user.core.enums.VerificationStatus
import com.garihub.user.core.exceptions.AuthException
import com.garihub.user.core.exceptions.DBException
import com.garihub.user.core.exceptions.NotFoundException
import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.interactor.UseCase
import com.garihub.user.core.exceptions.UserVerificationException
import com.garihub.user.core.exceptions.VerificationTokenExpiredException
import com.garihub.user.core.gateways.auth.AuthServiceApi

class VerifyRiderEmailAddressUseCase(private val dataStore: DataStore, private val authServiceApi: AuthServiceApi) :
    UseCase<VerificationToken, VerificationStatus>() {

    override fun execute(params: VerificationToken?): VerificationStatus {
        requireNotNull(params) { "Verification token can not be null" }

        @Suppress("TooGenericExceptionCaught")
        return try {
            val email = dataStore.verifyUserVerificationToken(params)

            // verify user account in Auth Service
            val externalIdentifier = authServiceApi.verifyRiderEmailAddress(email)

            if (externalIdentifier.isNullOrEmpty()) {
                VerificationStatus.FAILED_VERIFICATION
            } else {
                // update user with external identifier from auth service. This marks completion of user registration
                // flow without confirming their phone number
                dataStore.updateWithExternalIdentifier(email, externalIdentifier)
                VerificationStatus.VERIFIED
            }
        } catch (e: Exception) {
            return when (e) {
                is UserVerificationException -> VerificationStatus.FAILED_VERIFICATION
                is NotFoundException -> VerificationStatus.USER_NOT_FOUND
                is VerificationTokenExpiredException -> VerificationStatus.TOKEN_EXPIRED
                is AuthException -> VerificationStatus.AUTHENTICATION_FAILED
                is DBException -> VerificationStatus.FAILED_VERIFICATION
                else -> VerificationStatus.FAILED_VERIFICATION
            }
        }
    }
}
