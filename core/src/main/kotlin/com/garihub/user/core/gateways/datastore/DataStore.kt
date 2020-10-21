package com.garihub.user.core.gateways.datastore

import com.garihub.user.core.EmailAddress
import com.garihub.user.core.UserExternalIdentifier
import com.garihub.user.core.models.User
import com.garihub.user.core.UserIdentifier
import com.garihub.user.core.exceptions.DBException
import com.garihub.user.core.exceptions.NotFoundException
import com.garihub.user.core.exceptions.UserException
import com.garihub.user.core.exceptions.UserRegisteredException
import com.garihub.user.core.exceptions.UserVerificationException
import com.garihub.user.core.exceptions.VerificationTokenExpiredException

interface DataStore {
    /**
     * Verifies user token
     * @return [Boolean] True if the verification is still valid
     * @return [EmailAddress] Email address
     */
    @Throws(
        UserVerificationException::class,
        NotFoundException::class,
        VerificationTokenExpiredException::class,
        DBException::class
    )
    fun verifyUserVerificationToken(token: String): EmailAddress

    /**
     * Gets a single user from store
     */
    @Throws(UserException::class, DBException::class)
    fun getItem(identifier: UserIdentifier): User?

    /**
     * Creates a single user
     */
    @Throws(UserRegisteredException::class, DBException::class)
    fun createUser(user: User): UserIdentifier

    /**
     * Update a user given their identifier
     */
    @Throws(UserException::class, DBException::class)
    fun update(identifier: UserIdentifier, user: User): Boolean

    /**
     * Update a user given their external identifier as provided by auth service
     * @param emailAddress [EmailAddress] Email address of user to use whilst updating
     * @param identifier [UserExternalIdentifier] External identifier received from Auth Service
     */
    @Throws(UserException::class, DBException::class)
    fun updateWithExternalIdentifier(emailAddress: EmailAddress, identifier: UserExternalIdentifier): Boolean

    /**
     * Deletes a given user based on the identifier
     */
    @Throws(UserException::class, DBException::class)
    fun deregister(identifier: UserIdentifier): Boolean
}
