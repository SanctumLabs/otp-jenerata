package com.sanctumlabs.otp.domain.generators

import com.sanctumlabs.otp.core.services.GeneratedOtpCode
import com.sanctumlabs.otp.core.services.OtpCodeGenerator
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


/**
 * Configuration for TimeBased Code generator configuration
 * @param codeDigits [Int] number of digits to use for the OTP code
 * @param hmacAlgorithm  [HmacAlgorithms] HMAC algorithm to use
 * @param timeStep [Long] Duration in which OTP is valid
 * @param timeStepUnit [TimeUnit] Time Unit for the [timeStep]
 */
data class TimeBasedCodeGeneratorConfig(
    val codeDigits: Int,
    val hmacAlgorithm: HmacAlgorithms = HmacAlgorithms.SHA1,
    val timeStep: Long,
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
        val startEpochMills = generator.timeslotStart(counter)
        val endEpochMills = generator.timeslotStart(counter + 1) - 1

        // number of milliseconds the current OTP is still valid
        val millisValid = endEpochMills - startEpochMills

        val endTimeSlot = currentTime.plusMillis(millisValid)
        val expiryTime = LocalDateTime
            .ofInstant(endTimeSlot, ZoneId.systemDefault())
            .toKotlinLocalDateTime()

        return GeneratedOtpCode(
            code = otpCode,
            expiryTime = expiryTime
        )
    }
}
