package com.garihub.otp.database

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity(name = "userOtp")
@Table(name = "user_otps")
data class UserOtpEntity(
    @Column(name = "otp_code")
    var otpCode: String,

    @Column(name = "phone_number")
    var phoneNumber: String,

    @Column(name = "expiry_time")
    var expiryTime: LocalDateTime,

    @Column(name = "otp_used")
    var otpUsed: Boolean = false
) : BaseEntity()