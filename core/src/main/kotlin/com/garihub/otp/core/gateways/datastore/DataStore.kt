package com.garihub.otp.core.gateways.datastore

import com.garihub.otp.core.MobilePhoneNumber
import com.garihub.otp.core.OtpCode
import com.garihub.otp.core.exceptions.DBException
import com.garihub.otp.core.exceptions.NotFoundException
import com.garihub.otp.core.models.UserOtp

interface DataStore {
    /**
     * Finds user by otp code & phone number
     * @param otpCode [OtpCode] passed in OTP code
     * @param phoneNumber [MobilePhoneNumber] Mobile Phone Number
     * @return [UserOtp] User OTP entity
     */
    @Throws(NotFoundException::class, DBException::class)
    fun getByOtpCodeAndPhoneNumber(otpCode: OtpCode, phoneNumber: MobilePhoneNumber): UserOtp?

    /**
     * Find a User Otp by phone number if exists
     */
    @Throws(NotFoundException::class, DBException::class)
    fun getByPhoneNumber(phoneNumber: MobilePhoneNumber): UserOtp?

    /**
     * Save generated OTP or updates an existing OTP
     */
    @Throws(DBException::class)
    fun saveOtpCode(userOtp: UserOtp): Boolean

    /**
     * Marks OTP code as used
     */
    @Throws(DBException::class)
    fun markOtpAsUsed(userOtp: UserOtp): Boolean
}
