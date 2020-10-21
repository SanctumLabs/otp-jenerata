package com.garihub.user.core.gateways.auth

import com.garihub.user.core.EmailAddress
import com.garihub.user.core.UserExternalIdentifier
import com.garihub.user.core.exceptions.NotFoundException
import com.garihub.user.core.models.Gender
import kotlin.jvm.Throws

/**
 * Auth Service that handles authentication, registration of users
 */
interface AuthServiceApi {

    @Suppress("LongParameterList")
    fun registerRiderUser(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        gender: Gender
    ): Boolean

    /**
     * Check if a user is already registered with the given email address
     * @param email [String] Email Address
     */
    @Throws(NotFoundException::class)
    fun isRiderUserRegistered(email: String): Boolean

    /**
     * Verifies a Rider's email address with Auth Server
     * @param email [EmailAddress] email address
     * @return [UserExternalIdentifier] Valid external identifier if successfully updates & enables user account
     */
    fun verifyRiderEmailAddress(email: EmailAddress): UserExternalIdentifier?
}
