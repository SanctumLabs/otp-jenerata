package com.garihub.user.database

import com.garihub.user.core.EmailAddress
import com.garihub.user.core.UserExternalIdentifier
import com.garihub.user.core.exceptions.UserException
import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.models.User
import com.garihub.user.core.UserIdentifier
import com.garihub.user.core.exceptions.DBException
import com.garihub.user.core.exceptions.NotFoundException
import com.garihub.user.core.exceptions.UserRegisteredException
import com.garihub.user.core.exceptions.VerificationTokenExpiredException
import com.garihub.user.core.models.UserAccountStatus
import com.garihub.user.core.utils.generateIdentifier
import com.garihub.user.database.user.UserEntity
import com.garihub.user.database.user.UserRepository
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Suppress("TooGenericExceptionCaught")
class DataStoreImpl(private val userRepository: UserRepository) : DataStore {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun verifyUserVerificationToken(token: String): EmailAddress {
        logger.info("Verifying user token $token")

        val user = userRepository.findByVerifyToken(token) ?: throw NotFoundException("User token not found or invalid")

        val instant: Instant = Instant.now()
        val now = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)

        val expiryDate = user.verifyExpiryDate
        val isExpired = now.isAfter(expiryDate)

        if (isExpired) {
            throw VerificationTokenExpiredException("It appears this token has expired")
        }

        user.verifyToken = null
        user.verifyExpiryDate = null
        user.emailVerified = true
        user.accountStatus = UserAccountStatus.VERIFIED

        @Suppress("TooGenericExceptionCaught")
        return try {
            userRepository.save(user)
            user.emailAddress
        } catch (e: Exception) {
            logger.error("Failed to verify user token, Err: ${e.message}")
            throw DBException("Failed to update user")
        }
    }

    override fun getItem(identifier: UserIdentifier): User? {
        val userEntity = userRepository.findByIdentifier(identifier) ?: return null
        return User(
            identifier = userEntity.identifier,
            firstName = userEntity.firstName,
            lastName = userEntity.lastName,
            gender = userEntity.gender,
            phoneNumber = userEntity.phoneNumber,
            emailAddress = userEntity.emailAddress,
            userType = userEntity.userType
        )
    }

    override fun createUser(user: User): UserIdentifier {
        val foundUser = userRepository.findByPhoneNumber(user.phoneNumber)

        if (foundUser == null) {
            val name = "${user.firstName}:${user.lastName}"
            val identifier = generateIdentifier("$name:${user.emailAddress}:${user.phoneNumber}")

            val userEntity = UserEntity(
                identifier = identifier,
                firstName = user.firstName,
                lastName = user.lastName,
                gender = user.gender,
                phoneNumber = user.phoneNumber,
                emailAddress = user.emailAddress,
                userType = user.userType
            )

            return try {
                userRepository.save(userEntity)
                identifier
            } catch (e: Exception) {
                logger.error("Failed to create user ${e.message}")
                throw DBException("Failed to create user. Err: ${e.message}")
            }
        }

        throw UserRegisteredException("User already exists with the given phone number ${user.phoneNumber}")
    }

    override fun update(identifier: UserIdentifier, user: User): Boolean {
        val userEntity = userRepository.findByIdentifier(identifier) ?: return false
        val updatedUserEntity = userEntity.apply {
            this.firstName = user.firstName
            this.lastName = user.lastName
            this.gender = user.gender
            this.phoneNumber = user.phoneNumber
            this.emailAddress = user.emailAddress
            this.userType = user.userType
        }

        return try {
            userRepository.save(updatedUserEntity)
            true
        } catch (e: Exception) {
            logger.error("Failed to update user $identifier")
            throw UserException("Failed to update user")
        }
    }

    override fun updateWithExternalIdentifier(emailAddress: EmailAddress, identifier: UserExternalIdentifier): Boolean {
        val user = userRepository.findByEmailAddress(emailAddress) ?: return false

        return try {
            user.externalIdentifier = identifier
            userRepository.save(user)
            true
        } catch (e: Exception) {
            logger.error("Failed to update user $emailAddress with external identifier $identifier. Err: ${e.message}")
            throw DBException("Failed to update user")
        }
    }

    override fun deregister(identifier: UserIdentifier): Boolean {
        val event = userRepository.findByIdentifier(identifier) ?: return false

        return try {
            userRepository.delete(event)
            true
        } catch (e: Exception) {
            logger.error("Failed to delete event item ${e.message}")
            throw UserException("Failed to delete user $identifier")
        }
    }
}
