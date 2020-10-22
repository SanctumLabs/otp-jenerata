package com.garihub.otp.core.utils

import com.garihub.otp.core.models.UserOtp
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.concurrent.TimeUnit

private const val AMOUNT_TO_ADD = 60L

private val config = TimeBasedOneTimePasswordConfig(
    codeDigits = 6,
    hmacAlgorithm = HmacAlgorithm.SHA1,
    timeStep = 60, //duration in which otp is valid
    timeStepUnit = TimeUnit.SECONDS
)

fun generateOtp(otpKey: String, phoneNumber: String): UserOtp {
    val currentTime = Instant.now()
    val nextExpiry = currentTime.plus(AMOUNT_TO_ADD, ChronoUnit.SECONDS)
    val expiryTime = LocalDateTime.ofInstant(nextExpiry, ZoneOffset.UTC)

    val generationKey = "${otpKey}{$phoneNumber}"
    val codeGenerator = getGenerator(generationKey)
    val epochSeconds = currentTime.toEpochMilli()
    val date = Date(epochSeconds)
    val otpCode = codeGenerator.generate(timestamp = date)

    return UserOtp(otpCode = otpCode, phoneNumber = phoneNumber, expiryTime = expiryTime)
}

fun verifyOtp(otpEntity: UserOtp): Boolean {
    val instant: Instant = Instant.now()
    val now = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    val expiryDate = otpEntity.expiryTime
    return now.isBefore(expiryDate)
}

private fun getGenerator(otpKey: String): TimeBasedOneTimePasswordGenerator {
    return TimeBasedOneTimePasswordGenerator(otpKey.toByteArray(), config)
}
