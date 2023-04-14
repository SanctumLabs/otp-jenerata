package com.sanctumlabs.otp.domain.generators

import com.sanctumlabs.otp.core.services.GeneratedOtpCode
import com.sanctumlabs.otp.core.services.OtpCodeGenerator
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit


data class TimeBasedCodeGeneratorConfig(
    val codeDigits: Int,
    val hmacAlgorithm: HmacAlgorithms = HmacAlgorithms.SHA1,
    val timeStep: Long, //duration in which otp is valid
    val timeStepUnit: TimeUnit = TimeUnit.SECONDS
)

class TimeBasedOtpCodeGenerator(private val key: String, private val config: TimeBasedCodeGeneratorConfig) :
    OtpCodeGenerator {

    override fun generate(value: String): GeneratedOtpCode {
        val (codeDigits, hmacAlgorithm, timeStep, timeStepUnit) = config

        val timeConfig = TimeBasedOneTimePasswordConfig(
            codeDigits = codeDigits,
            hmacAlgorithm = when (hmacAlgorithm) {
                HmacAlgorithms.SHA256 -> HmacAlgorithm.SHA256
                HmacAlgorithms.SHA1 -> HmacAlgorithm.SHA1
                HmacAlgorithms.SHA512 -> HmacAlgorithm.SHA512
            },
            timeStep = timeStep,
            timeStepUnit = timeStepUnit
        )

        val secret = "${key}{$value}"
        val encodedSecret = secret.toByteArray()

        val generator = TimeBasedOneTimePasswordGenerator(secret = encodedSecret, config = timeConfig)

        val currentTime = Instant.now()
        val timestamp = currentTime.toEpochMilli()

        val otpCode = generator.generate(timestamp = timestamp)
        val counter = generator.counter()

        // the start of next time slot minus 1ms
        val endEpochMills = generator.timeslotStart(counter + 1) - 1

        // number of milliseconds the current OTP is still valid
        val millisValid = endEpochMills - timestamp

        val expiryTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millisValid), ZoneOffset.UTC)

        return GeneratedOtpCode(
            code = otpCode,
            expiryTime = expiryTime
        )
    }
}
