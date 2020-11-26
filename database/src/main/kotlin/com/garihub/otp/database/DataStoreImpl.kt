package com.garihub.otp.database

import com.garihub.otp.core.PhoneNumberOrEmail
import com.garihub.otp.core.OtpCode
import com.garihub.otp.core.exceptions.DBException
import com.garihub.otp.core.exceptions.NotFoundException
import com.garihub.otp.core.gateways.datastore.DataStore
import com.garihub.otp.core.models.UserOtp
import org.slf4j.LoggerFactory

@Suppress("TooGenericExceptionCaught")
class DataStoreImpl(private val userOtpRepository: UserOtpRepository) : DataStore {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getByOtpCodeAndPhoneNumber(otpCode: OtpCode, phoneNumberOrEmail: PhoneNumberOrEmail): UserOtp? {
        val userOtp = userOtpRepository.findUserOtpByOtpCodeAndPhoneNumber(otpCode, phoneNumberOrEmail)
            ?: throw NotFoundException("User OTP not found")
        try {
            return UserOtp(
                otpCode = userOtp.otpCode,
                phoneNumberOrEmail = userOtp.phoneNumberOrEmail,
                expiryTime = userOtp.expiryTime,
                otpUsed = userOtp.otpUsed
            )
        } catch (e: Exception) {
            logger.error("Failed to get UserOtp for Code: $otpCode & Phone: $phoneNumberOrEmail. Err: ${e.message}")
            throw DBException("Failed to get UserOtp. Err: ${e.message}")
        }
    }

    override fun getByPhoneNumber(phoneNumberOrEmail: PhoneNumberOrEmail): UserOtp? {
        val userOtp = userOtpRepository.findUserOtpByPhoneNumberOrEmail(phoneNumberOrEmail) ?: throw NotFoundException("User OTP not found")

        try {
            return UserOtp(
                otpCode = userOtp.otpCode,
                phoneNumberOrEmail = userOtp.phoneNumberOrEmail,
                expiryTime = userOtp.expiryTime,
                otpUsed = userOtp.otpUsed
            )
        } catch (e: Exception) {
            logger.error("Failed to get UserOtp for Phone: $phoneNumberOrEmail. Err: ${e.message}")
            throw DBException("Failed to get UserOtp. Err: ${e.message}")
        }
    }

    override fun saveOtpCode(userOtp: UserOtp): Boolean {
        val existingUserOtp = userOtpRepository.findUserOtpByPhoneNumberOrEmail(userOtp.phoneNumberOrEmail)

        try {
            // we are updating it
            return if (existingUserOtp != null) {
                existingUserOtp.expiryTime = userOtp.expiryTime
                existingUserOtp.otpCode = userOtp.otpCode
                userOtpRepository.save(existingUserOtp)
                true
            } else {
                // this is a new record
                val userOtpEntity = UserOtpEntity(
                    otpCode = userOtp.otpCode,
                    phoneNumberOrEmail = userOtp.phoneNumberOrEmail,
                    expiryTime = userOtp.expiryTime
                )
                userOtpRepository.save(userOtpEntity)
                true
            }
        } catch (e: Exception) {
            logger.error("Failed to save OTP code. Err: ${e.message}")
            throw DBException("Failed to save OTP")
        }
    }

    override fun markOtpAsUsed(userOtp: UserOtp): Boolean {
        val existingUserOtp =
            userOtpRepository.findUserOtpByPhoneNumberOrEmail(userOtp.phoneNumberOrEmail) ?: throw NotFoundException("User OTP not found")
        existingUserOtp.otpUsed = true

        return try {
            userOtpRepository.save(existingUserOtp)
            true
        } catch (e: Exception) {
            logger.error("Failed to mark OTP as used. Err: ${e.message}")
            throw DBException("Failed to mark OTP as used")
        }
    }
}
