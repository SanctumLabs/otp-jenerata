package com.sanctumlabs.otp.domain.generators

import com.sanctumlabs.otp.core.services.OtpCodeGenerator
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import java.time.Instant
import java.util.concurrent.TimeUnit

enum class HmacAlgo {
    SHA1,
    SHA256,
    SHA512
}

data class TimeBaseCodeGeneratorConfig(
    val codeDigits: Int,
    val timeStep: Long, //duration in which otp is valid
    val hmacAlgorithm: HmacAlgo = HmacAlgo.SHA1,
    val timeStepUnit: TimeUnit = TimeUnit.SECONDS
)

class TimeBasedOtpCodeGenerator(
    private val key: String, private val config: TimeBaseCodeGeneratorConfig = TimeBaseCodeGeneratorConfig(
        codeDigits = 6,
        hmacAlgorithm = HmacAlgo.SHA1,
        timeStep = 180,
        timeStepUnit = TimeUnit.SECONDS
    )
) : OtpCodeGenerator {

    override fun generate(value: String): String {
        val timeConfig = TimeBasedOneTimePasswordConfig(
            codeDigits = config.codeDigits,
            hmacAlgorithm = when (config.hmacAlgorithm) {
                HmacAlgo.SHA256 -> HmacAlgorithm.SHA256
                HmacAlgo.SHA1 -> HmacAlgorithm.SHA1
                HmacAlgo.SHA512 -> HmacAlgorithm.SHA512
            },
            timeStep = config.timeStep, //duration in which otp is valid
            timeStepUnit = config.timeStepUnit
        )

        val secret = "${key}{$value}"

        val currentTime = Instant.now()
        val timestamp = currentTime.toEpochMilli()

        val generator = TimeBasedOneTimePasswordGenerator(secret = secret.toByteArray(), config = timeConfig)
        val totp = generator.generate(timestamp = timestamp)
        val counter = generator.counter()
        val startEpochMillis = generator.timeslotStart(counter)
        val endEpochMillis = generator.timeslotStart(counter + 1) - 1
        val millisValid = endEpochMillis - timestamp

        return totp
    }
}
