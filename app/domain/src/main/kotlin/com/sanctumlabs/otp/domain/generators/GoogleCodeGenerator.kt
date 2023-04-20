package com.sanctumlabs.otp.domain.generators

import com.sanctumlabs.otp.core.services.GeneratedOtpCode
import com.sanctumlabs.otp.core.services.OtpCodeGenerator
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

// number of seconds the current OTP is still valid
private const val AMOUNT_TO_ADD_IN_SECONDS = 30

class GoogleCodeGenerator(private val key: String) : OtpCodeGenerator {

    override fun generate(value: String): GeneratedOtpCode {
        val encodedSecret = key.toByteArray(Charsets.UTF_8)

        val currentTime = Date(System.currentTimeMillis())

        val generator = GoogleAuthenticator(encodedSecret)

        val otpCode = generator.generate(timestamp = currentTime)

        val calendar = Calendar.getInstance()
        calendar.time = currentTime
        calendar.add(Calendar.SECOND, AMOUNT_TO_ADD_IN_SECONDS)

        val expiryTime = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE)
        ).toKotlinLocalDateTime()

        return GeneratedOtpCode(
            code = otpCode,
            expiryTime = expiryTime
        )
    }
}
