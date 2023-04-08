package com.sanctumlabs.otp.domain.generators

import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit

class TimeBasedCodeGenerator(
    private val key: String, private val config: TimeBasedOneTimePasswordConfig = TimeBasedOneTimePasswordConfig(
        codeDigits = 6,
        hmacAlgorithm = HmacAlgorithm.SHA1,
        timeStep = 180, //duration in which otp is valid
        timeStepUnit = TimeUnit.SECONDS
    )
) {

    fun generate(secret: String): String {
        val generationKey = "${key}{$secret}"
        val currentTime = Instant.now()

        val generator = TimeBasedOneTimePasswordGenerator(generationKey.toByteArray(), config)
        val epochSeconds = currentTime.toEpochMilli()
        val date = Date(epochSeconds)
        return generator.generate(timestamp = date.toInstant().toEpochMilli())
    }
}
