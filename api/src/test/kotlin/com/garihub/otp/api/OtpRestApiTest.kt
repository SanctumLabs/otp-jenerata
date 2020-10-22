package com.garihub.otp.api

import com.garihub.otp.api.dto.UserOtpDto
import com.garihub.otp.api.dto.UserOtpResponseDto
import com.garihub.otp.api.dto.UserVerifyOtpDto
import com.garihub.otp.core.enums.OtpVerificationStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException
import java.util.Collections

class OtpRestApiTest {

    private val mockUserService = mockk<OtpService>()
    private var otpRestApi: OtpRestApi = OtpRestApi(mockUserService)

    private val otpCode = "123456"

    private val userOtpDto = UserOtpDto(
        phoneNumber = "254700000000"
    )
    private val userOtpResponseDto = UserOtpResponseDto(
        phoneNumber = "254700000000",
        otpCode = otpCode
    )

    private val userVerifyOtpDto = UserVerifyOtpDto(
        phoneNumber = "254700000000",
        otpCode = otpCode
    )

    @Test
    fun `Should return user OTP when generation of an OTP succeeds`() {
        every {
            mockUserService.generateOtp(userOtpDto)
        } returns otpCode

        val expectedResponse = ResponseEntity(userOtpResponseDto, HttpStatus.OK)

        val actual = otpRestApi.generateOtp(userOtpDto)
        Assertions.assertEquals(expectedResponse, actual)
    }

    @Test
    fun `Should return exception when user OTP generation returns null`() {
        every {
            mockUserService.generateOtp(userOtpDto)
        } returns null

        val actual = Assertions.assertThrows(ResponseStatusException::class.java) { otpRestApi.generateOtp(userOtpDto) }
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.status)
    }

    @Test
    fun `Should return OTP VERIFIED when OTP can be successfully verified`() {
        every {
            mockUserService.verifyOtp(userVerifyOtpDto)
        } returns OtpVerificationStatus.VERIFIED

        val expectedResponse =
            ResponseEntity(
                Collections.singletonMap("message", "Successfully Verified"),
                HttpStatus.OK
            )

        val actual = otpRestApi.verifyOtp(userVerifyOtpDto)
        Assertions.assertEquals(expectedResponse, actual)
    }

    @Test
    fun `Should return FAILED_VERIFICATION when OTP can not be successfully verified`() {
        every {
            mockUserService.verifyOtp(userVerifyOtpDto)
        } returns OtpVerificationStatus.FAILED_VERIFICATION

        val expectedResponse =
            ResponseEntity(
                Collections.singletonMap("message", "Failed to verify otp"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )

        val actual = otpRestApi.verifyOtp(userVerifyOtpDto)
        Assertions.assertEquals(expectedResponse, actual)
    }

    @Test
    fun `Should return Token Expired when OTP has expired`() {
        every {
            mockUserService.verifyOtp(userVerifyOtpDto)
        } returns OtpVerificationStatus.TOKEN_EXPIRED

        val expectedResponse =
            ResponseEntity(Collections.singletonMap("message", "Expired OTP"), HttpStatus.BAD_REQUEST)

        val actual = otpRestApi.verifyOtp(userVerifyOtpDto)
        Assertions.assertEquals(expectedResponse, actual)
    }

    @Test
    fun `Should return 404 when OTP for user can not be found`() {
        every {
            mockUserService.verifyOtp(userVerifyOtpDto)
        } returns OtpVerificationStatus.USER_NOT_FOUND

        val expectedResponse =
            ResponseEntity(
                Collections.singletonMap("message", "User not found or token expired"), HttpStatus.NOT_FOUND
            )

        val actual = otpRestApi.verifyOtp(userVerifyOtpDto)
        Assertions.assertEquals(expectedResponse, actual)
    }
}
