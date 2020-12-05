package com.garihub.otp.api

import com.garihub.otp.api.dto.UserOtpDto
import com.garihub.otp.api.dto.UserVerifyOtpDto
import com.garihub.otp.core.enums.OtpVerificationStatus
import com.garihub.otp.core.models.UserVerifyOtp
import com.garihub.otp.core.usecases.GenerateOtpUseCase
import com.garihub.otp.core.usecases.VerifyOtpUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class OtpServiceTest {

    private val mockGenerateOtpUseUseCase = mockk<GenerateOtpUseCase>()
    private val mockVerifyOtpUseCase = mockk<VerifyOtpUseCase>()

    private val userService by lazy {
        OtpService(
            mockGenerateOtpUseUseCase,
            mockVerifyOtpUseCase
        )
    }

    private val otpCode = "908632"
    private val phoneNumber = "254700000000"
    private val invalidPhoneNumber = "700000000"

    private val userOtpDto = UserOtpDto(
        phoneNumber = "254700000000"
    )
    private val userOtpInvalidDto = UserOtpDto(
        phoneNumber = invalidPhoneNumber
    )

    private val userVerifyOtpDto = UserVerifyOtpDto(
        phoneNumberOrEmail = "+254700000000",
        otpCode = otpCode
    )

    private val userVerifyOtp = UserVerifyOtp(
        phoneNumberOrEmail = "+254700000000",
        otpCode = otpCode
    )

    @Test
    fun `Should return Failed OTP Verification status when usecase fails`() {
        every {
            mockVerifyOtpUseCase.execute(userVerifyOtp)
        } throws Exception("Failed verification")

        val actual = userService.verifyOtp(userVerifyOtpDto)

        Assertions.assertEquals(OtpVerificationStatus.FAILED_VERIFICATION, actual)
    }

    @Test
    fun `Should return OTP Verification status when use case returns verification status`() {
        val verificationStatusList = listOf(
            OtpVerificationStatus.VERIFIED,
            OtpVerificationStatus.CODE_EXPIRED,
            OtpVerificationStatus.FAILED_VERIFICATION,
            OtpVerificationStatus.USER_NOT_FOUND
        )

        fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

        val expectedResponse = verificationStatusList.getRandomElement()

        every {
            mockVerifyOtpUseCase.execute(userVerifyOtp)
        } returns expectedResponse

        val actual = userService.verifyOtp(userVerifyOtpDto)

        Assertions.assertEquals(expectedResponse, actual)
    }

    @Test
    fun `Should return null if OTP generation fails`() {
        every {
            mockGenerateOtpUseUseCase.execute(phoneNumber)
        } throws Exception("Some Exception")

        val actual = userService.generateOtp(userOtpDto)

        Assertions.assertNull(actual)
    }

    @Test
    fun `Should return null if phone number is invalid`() {
        val actual = userService.generateOtp(userOtpInvalidDto)

        Assertions.assertNull(actual)
    }

    @Test
    fun `Should return OTP code if phone number is valid`() {
        every {
            mockGenerateOtpUseUseCase.execute(phoneNumber)
        } returns otpCode

        val actual = userService.generateOtp(userOtpDto)

        Assertions.assertEquals(otpCode, actual)
    }

}
