package com.garihub.otp.core.usecases

import com.garihub.otp.core.enums.OtpVerificationStatus
import com.garihub.otp.core.exceptions.NotFoundException
import com.garihub.otp.core.gateways.datastore.DataStore
import com.garihub.otp.core.models.UserOtp
import com.garihub.otp.core.models.UserVerifyOtp
import com.garihub.otp.core.utils.verifyOtp
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class VerifyOtpUseCaseTest {

    private val mockDataStore = mockk<DataStore>()

    private val verifyOtpUseCase by lazy {
        VerifyOtpUseCase(mockDataStore)
    }

    private val otpCode = "123456"
    private val mobilePhoneNumber = "254700000000"
    private val userVerifyOtp = UserVerifyOtp(
        otpCode = otpCode,
        phoneNumberOrEmail = mobilePhoneNumber
    )
    private val userOtp = UserOtp(
        otpCode = otpCode,
        phoneNumberOrEmail = mobilePhoneNumber,
        expiryTime = LocalDateTime.now()
    )

    @Test
    fun `Should throw IllegalArgumentException when executed without verification token`() {
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            verifyOtpUseCase.execute()
        }

        Assertions.assertEquals("OTP can not be null", actual.message)
    }

    @Test
    fun `Should throw NotFoundException when user otp can not be found for given OTP code & mobile number`() {
        every {
            mockDataStore.getByOtpCodeAndPhoneNumber(otpCode, mobilePhoneNumber)
        } returns null

        Assertions.assertThrows(NotFoundException::class.java) { verifyOtpUseCase.execute(userVerifyOtp) }
    }

    @Test
    fun `Should return OtpVerificationStatus VERIFIED when otp is successfully verified`() {
        every {
            mockDataStore.getByOtpCodeAndPhoneNumber(otpCode, mobilePhoneNumber)
        } returns userOtp

        mockkStatic("com.garihub.otp.core.utils.OtpUtilityKt")
        every {
            verifyOtp(userOtp)
        } returns true

        every {
            mockDataStore.markOtpAsUsed(userOtp)
        } returns true

        val actual = verifyOtpUseCase.execute(userVerifyOtp)

        Assertions.assertEquals(OtpVerificationStatus.VERIFIED, actual)
    }

    @Test
    fun `Should return CODE expired when otp is not successfully verified`() {
        every {
            mockDataStore.getByOtpCodeAndPhoneNumber(otpCode, mobilePhoneNumber)
        } returns userOtp

        mockkStatic("com.garihub.otp.core.utils.OtpUtilityKt")
        every {
            verifyOtp(userOtp)
        } returns false

        every {
            mockDataStore.markOtpAsUsed(userOtp)
        } returns true

        val actual = verifyOtpUseCase.execute(userVerifyOtp)

        Assertions.assertEquals(OtpVerificationStatus.CODE_EXPIRED, actual)
    }

    @Test
    fun `Should return Failed verification when datastore throws exception`() {
        every {
            mockDataStore.getByOtpCodeAndPhoneNumber(otpCode, mobilePhoneNumber)
        } returns userOtp

        mockkStatic("com.garihub.otp.core.utils.OtpUtilityKt")
        every {
            verifyOtp(userOtp)
        } returns true

        every {
            mockDataStore.markOtpAsUsed(userOtp)
        } throws Exception("Some Exception")

        val actual = verifyOtpUseCase.execute(userVerifyOtp)

        Assertions.assertEquals(OtpVerificationStatus.FAILED_VERIFICATION, actual)
    }
}
