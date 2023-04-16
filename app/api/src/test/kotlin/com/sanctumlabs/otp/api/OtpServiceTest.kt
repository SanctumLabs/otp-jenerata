package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpVerifyDto
import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.services.CreateOtpService
import com.sanctumlabs.otp.core.services.VerifyOtpService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class OtpServiceTest {
    private val mockCreateOtpService = mockk<CreateOtpService>()
    private val mockVerifyService = mockk<VerifyOtpService>()
    private val otpService by lazy {
        OtpService(
            mockCreateOtpService,
            mockVerifyService
        )
    }

    @Test
    fun `Should throw exception when there is a failure to verify OTP code`() {
        val otpCode = "908632"
        val userId = "254700000000"

        val verifyOtpCode = VerifyOtpCode(
            otpCode = otpCode,
            userId = UserId(userId)
        )

        val otpVerifyDto = OtpVerifyDto(
            userId = userId,
            code = otpCode
        )

        every {
            mockVerifyService.execute(any())
        } throws Exception("Failed verification")

        assertThrows<Exception> {
            otpService.verifyOtp(otpVerifyDto)
        }

        verify {
            mockVerifyService.execute(verifyOtpCode)
        }

        confirmVerified(mockVerifyService)
    }

    @Test
    fun `Should return OTP Verification status when verify otp service succeeds`() {
        val otpCode = "908632"
        val userId = "254700000000"

        val verificationStatusList = listOf(
            OtpVerificationStatus.VERIFIED,
            OtpVerificationStatus.CODE_EXPIRED,
            OtpVerificationStatus.FAILED_VERIFICATION,
            OtpVerificationStatus.USER_NOT_FOUND
        )

        fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

        val expectedResponse = verificationStatusList.getRandomElement()

        val verifyOtpCode = VerifyOtpCode(
            otpCode = otpCode,
            userId = UserId(userId)
        )

        val otpVerifyDto = OtpVerifyDto(
            userId = userId,
            code = otpCode
        )

        every {
            mockVerifyService.execute(any())
        } returns expectedResponse

        val actual = assertDoesNotThrow {
            otpService.verifyOtp(otpVerifyDto)
        }

        assertEquals(expectedResponse, actual)

        verify {
            mockVerifyService.execute(verifyOtpCode)
        }

        confirmVerified(mockVerifyService)
    }

    @Test
    fun `Should throw exception if OTP generation fails`() {
        val userId = "254700000000"
        val otpRequestDto = OtpRequestDto(
            userId = userId
        )

        every {
            mockCreateOtpService.execute(any())
        } throws Exception("Some Exception")

        assertThrows<Exception> {
            otpService.generateOtp(otpRequestDto)
        }

        verify {
            mockCreateOtpService.execute(UserId(userId))
        }

        confirmVerified(mockVerifyService)
    }

    @Test
    fun `Should return OTP code upon successful generation of code`() {
        val userId = "254700000000"
        val otpRequestDto = OtpRequestDto(
            userId = userId
        )

        val code = "12345"
        val user = UserId(userId)
        val expiryTime = LocalDateTime.now()

        val otpCode = OtpCode(
            code = code,
            userId = user,
            expiryTime = expiryTime,
            used = false
        )

        every {
            mockCreateOtpService.execute(any())
        } returns otpCode

        val actual = assertDoesNotThrow {
            otpService.generateOtp(otpRequestDto)
        }

        assertEquals(otpCode, actual)

        verify {
            mockCreateOtpService.execute(UserId(userId))
        }

        confirmVerified(mockVerifyService)
    }

}
