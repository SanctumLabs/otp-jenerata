package com.garihub.user.database.user

import com.garihub.user.core.EmailAddress
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByIdentifier(identifier: String): UserEntity?

    override fun findAll(): List<UserEntity>
    override fun findAll(pageable: Pageable): Page<UserEntity>

    fun findByPhoneNumber(phoneNumber: String): UserEntity?

    fun findByVerifyToken(verifyToken: String): UserEntity?

    /**
     * Find all users who have not had verification emails sent
     */
    fun findAllByEmailSentIsFalse(pageable: Pageable? = null): Page<UserEntity>

    /**
     * Find a user by their email address
     * @param emailAddress [EmailAddress] Email address to use
     * @return [UserEntity] nullable user entity if search result returns null or user entity
     */
    fun findByEmailAddress(emailAddress: EmailAddress): UserEntity?

//    fun findByResetToken(resetToken: String): UserEntity?
}
