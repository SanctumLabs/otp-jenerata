package com.garihub.user.database.user

import com.garihub.user.core.models.Gender
import com.garihub.user.core.UserIdentifier
import com.garihub.user.core.models.UserAccountStatus
import com.garihub.user.core.models.UserType
import com.garihub.user.database.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity(name = "user")
@Table(name = "users")
data class UserEntity(
    @Column(name = "identifier", nullable = false)
    var identifier: UserIdentifier,

    @Column(name = "external_identifier", nullable = true)
    var externalIdentifier: UserIdentifier? = null,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "TEXT")
    var gender: Gender,

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String,

    @Column(name = "email_address", nullable = false)
    var emailAddress: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", columnDefinition = "TEXT")
    var userType: UserType = UserType.RIDER,

    @Column(name = "verification_email_sent")
    var emailSent: Boolean = false,

    @Column(name = "email_verified")
    var emailVerified: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", columnDefinition = "TEXT")
    var accountStatus: UserAccountStatus = UserAccountStatus.NOT_VERIFIED,

    @Column(name = "verify_token")
    var verifyToken: String? = null,

    @Column(name = "verify_expiry_date")
    var verifyExpiryDate: LocalDateTime? = null
) : BaseEntity()
