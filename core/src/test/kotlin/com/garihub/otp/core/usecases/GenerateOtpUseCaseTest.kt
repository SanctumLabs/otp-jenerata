package com.garihub.otp.core.usecases

import com.garihub.otp.core.exceptions.OtpGenerationException
import com.garihub.otp.core.gateways.datastore.DataStore
import com.garihub.otp.core.models.UserOtp
import com.garihub.otp.core.utils.generateOtp
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class GenerateOtpUseCaseTest {
    private val mockDataStore = mockk<DataStore>()
    private val otpKey = "otpkey"
    private val generateOtpUseCase by lazy {
        GenerateOtpUseCase(mockDataStore, otpKey)
    }
    private val otpCode = "123456"
    private val mobilePhoneNumber = "254700000000"

    private val userOtp = UserOtp(
        otpCode = otpCode,
        phoneNumberOrEmail = mobilePhoneNumber,
        expiryTime = LocalDateTime.now()
    )

    @Test
    fun `Should throw illegal argument exception when executed with null params`() {
        val expectedErrorMsg = "Must pass in a valid phone number"
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            generateOtpUseCase.execute()
        }

        Assertions.assertEquals(expectedErrorMsg, actual.message)
    }

    @Test
    fun `Should throw OtpGenerationException when saving OTP in datastore fails`() {
        every {
            mockDataStore.saveOtpCode(userOtp)
        } throws Exception("Some Exception")

        Assertions.assertThrows(OtpGenerationException::class.java) { generateOtpUseCase.execute(mobilePhoneNumber) }
    }

    @Test
    fun `Should return null when otp generation is a success, but saving in data store fails`() {
        mockkStatic("com.garihub.otp.core.utils.OtpUtilityKt")
        every {
            generateOtp(otpKey, mobilePhoneNumber)
        } returns userOtp

        every {
            mockDataStore.saveOtpCode(userOtp)
        } returns false

        val actual = generateOtpUseCase.execute(mobilePhoneNumber)

        Assertions.assertNull(actual)
    }

    @Test
    fun `Should return otp code when otp generation is a success & saving in data store succeeds`() {
        mockkStatic("com.garihub.otp.core.utils.OtpUtilityKt")
        every {
            generateOtp(otpKey, mobilePhoneNumber)
        } returns userOtp

        every {
            mockDataStore.saveOtpCode(userOtp)
        } returns true

        val actual = generateOtpUseCase.execute(mobilePhoneNumber)

        Assertions.assertEquals(otpCode, actual)
    }
}
