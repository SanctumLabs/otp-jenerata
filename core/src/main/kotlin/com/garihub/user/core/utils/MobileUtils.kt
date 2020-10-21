package com.garihub.user.core.utils

import com.garihub.user.core.exceptions.InvalidPhoneNumberException
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

/**
 * Checks if a phone number is valid
 * @param phoneNumber [String] Phone number to validate
 * @return [Boolean] true if the phone number is valid, false otherwise
 * Reference: https://github.com/google/libphonenumber
 */
fun isPhoneNumberValid(phoneNumber: String): Boolean {
    val phoneUtil = PhoneNumberUtil.getInstance()

    val mobilePhoneNumber = try {
        // null means no default region
        val phoneNumberProto = phoneUtil.parse("+$phoneNumber", null)
        Phonenumber.PhoneNumber()
            .setCountryCode(phoneNumberProto.countryCode)
            .setNationalNumber(phoneNumberProto.nationalNumber)
    } catch (npe: NumberParseException) {
        throw InvalidPhoneNumberException("Number Parse Exception ${npe.message}")
    }

    return phoneUtil.isValidNumber(mobilePhoneNumber)
}
