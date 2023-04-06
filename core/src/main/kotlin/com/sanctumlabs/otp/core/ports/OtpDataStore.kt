package com.sanctumlabs.otp.core.ports

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.PhoneNumberOrEmail
import com.sanctumlabs.otp.core.exceptions.DBException
import com.sanctumlabs.otp.core.exceptions.NotFoundException

interface OtpDataStore {
    /**
     * Finds user by otp code & phone number
     * @param otpCode [OtpCode] passed in OTP code
     * @param phoneNumberOrEmail [PhoneNumberOrEmail] Mobile Phone Number
     * @return [OtpCode] User OTP entity
     */
    @Throws(NotFoundException::class, DBException::class)
    fun getByOtpCodeAndPhoneNumber(otpCode: OtpCode, phoneNumberOrEmail: PhoneNumberOrEmail): OtpCode?

    /**
     * Find a User Otp by phone number if exists
     */
    @Throws(NotFoundException::class, DBException::class)
    fun getByPhoneNumber(phoneNumberOrEmail: PhoneNumberOrEmail): OtpCode?

    /**
     * Save generated OTP or updates an existing OTP
     */
    @Throws(DBException::class)
    fun saveOtpCode(otpCode: OtpCode): Boolean

    /**
     * Marks OTP code as used
     */
    @Throws(DBException::class)
    fun markOtpAsUsed(otpCode: OtpCode): Boolean
}
