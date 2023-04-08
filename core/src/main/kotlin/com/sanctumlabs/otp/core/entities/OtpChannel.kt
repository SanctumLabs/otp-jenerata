package com.sanctumlabs.otp.core.entities

sealed class OtpChannel(open val value: String)

data class PhoneNumber(override val value: String) : OtpChannel(value)

data class Email(override val value: String) : OtpChannel(value)
