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
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*
import kotlin.test.assertEquals

@Tag("unit")
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
    fun `Should throw exception when there is a failure to coVerify OTP code`() {
        val otpCode = "908632"
        val userId = "254700000000"

        val coVerifyOtpCode = VerifyOtpCode(
            otpCode = otpCode,
            userId = UserId(userId)
        )

        val otpVerifyDto = OtpVerifyDto(
            userId = userId,
            code = otpCode
        )

        coEvery {
            mockVerifyService.execute(any())
        } throws Exception("Failed verification")

        assertThrows<Exception> {
            runBlocking {
                otpService.verifyOtp(otpVerifyDto)
            }
        }

        coVerify {
            mockVerifyService.execute(coVerifyOtpCode)
        }

        confirmVerified(mockVerifyService)
    }

    @Test
    fun `Should return OTP Verification status when coVerify otp service succeeds`() {
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

        val coVerifyOtpCode = VerifyOtpCode(
            otpCode = otpCode,
            userId = UserId(userId)
        )

        val otpVerifyDto = OtpVerifyDto(
            userId = userId,
            code = otpCode
        )

        coEvery {
            mockVerifyService.execute(any())
        } returns expectedResponse

        val actual = assertDoesNotThrow {
            runBlocking {
                otpService.verifyOtp(otpVerifyDto)
            }
        }

        assertEquals(expectedResponse, actual)

        coVerify {
            mockVerifyService.execute(coVerifyOtpCode)
        }

        confirmVerified(mockVerifyService)
    }

    @Test
    fun `Should throw exception if OTP generation fails`() {
        val userId = "254700000000"
        val otpRequestDto = OtpRequestDto(
            userId = userId
        )

        coEvery {
            mockCreateOtpService.execute(any())
        } throws Exception("Some Exception")

        assertThrows<Exception> {
            runBlocking {
                otpService.generateOtp(otpRequestDto)
            }
        }

        coVerify {
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
        val expiryTime = LocalDateTime(LocalDate(2023, 1, 1), LocalTime(1, 1, 1))

        val otpCode = OtpCode(
            code = code,
            userId = user,
            expiryTime = expiryTime,
            used = false
        )

        coEvery {
            mockCreateOtpService.execute(any())
        } returns otpCode

        val actual = assertDoesNotThrow {
            runBlocking {
                otpService.generateOtp(otpRequestDto)
            }
        }

        assertEquals(otpCode, actual)

        coVerify {
            mockCreateOtpService.execute(UserId(userId))
        }

        confirmVerified(mockVerifyService)
    }
}
