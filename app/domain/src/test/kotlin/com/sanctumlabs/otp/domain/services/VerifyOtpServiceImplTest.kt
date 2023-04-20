package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.datetime.toKotlinLocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class VerifyOtpServiceImplTest {
    private val mockDataStore = mockk<OtpDataStore>()
    private val verifyOtpService = VerifyOtpServiceImpl(mockDataStore)

    @Test
    fun `should throw NotFoundException if OTP can not be found`() {
        every {
            mockDataStore.getOtpCode(any())
        } throws NotFoundException("OTP code not found")

        val request = VerifyOtpCode(
            otpCode = "123456",
            userId = UserId("09876")
        )

        assertThrows<NotFoundException> {
            verifyOtpService.execute(request)
        }

        verify {
            mockDataStore.getOtpCode(request.otpCode)
        }
    }

    @Test
    fun `should return OtpVerificationStatus_VERIFIED if OTP expiry time is after now`() {
        val code = "123456"
        val userId = UserId("09876")
        val expiryTime = LocalDateTime.now().plusMinutes(5).toKotlinLocalDateTime()

        val otpCode = OtpCode(
            code = code,
            userId = userId,
            expiryTime = expiryTime
        )

        val updatedOtpCode = otpCode.copy(used = true)

        every {
            mockDataStore.getOtpCode(any())
        } returns otpCode

        justRun {
            mockDataStore.markOtpAsUsed(any())
        }

        val request = VerifyOtpCode(
            otpCode = code,
            userId = userId
        )

        val actual = assertDoesNotThrow {
            verifyOtpService.execute(request)
        }

        assertEquals(OtpVerificationStatus.VERIFIED, actual)

        verify {
            mockDataStore.getOtpCode(request.otpCode)
            mockDataStore.markOtpAsUsed(updatedOtpCode)
        }
    }

    @Test
    fun `should return OtpVerificationStatus_CODE_EXPIRED if OTP expiry time is before now`() {
        val code = "123456"
        val userId = UserId("09876")
        val expiryTime = LocalDateTime.now().minusMinutes(10).toKotlinLocalDateTime()

        val otpCode = OtpCode(
            code = code,
            userId = userId,
            expiryTime = expiryTime
        )

        every {
            mockDataStore.getOtpCode(any())
        } returns otpCode

        val request = VerifyOtpCode(
            otpCode = code,
            userId = userId
        )

        val actual = assertDoesNotThrow {
            verifyOtpService.execute(request)
        }

        assertEquals(OtpVerificationStatus.CODE_EXPIRED, actual)

        verify {
            mockDataStore.getOtpCode(request.otpCode)
        }

        verify(exactly = 0) {
            mockDataStore.markOtpAsUsed(any())
        }
    }

    @Test
    fun `should return OtpVerificationStatus_FAILED_VERIFICATION if there is a failure marking otp as used`() {
        val code = "123456"
        val userId = UserId("09876")
        val expiryTime = LocalDateTime.now().plusMinutes(10).toKotlinLocalDateTime()

        val otpCode = OtpCode(
            code = code,
            userId = userId,
            expiryTime = expiryTime
        )
        val updatedOtpCode = otpCode.copy(used = true)

        every {
            mockDataStore.getOtpCode(any())
        } returns otpCode

        every {
            mockDataStore.markOtpAsUsed(any())
        } throws Exception("Failed to mark OTP as used")

        val request = VerifyOtpCode(
            otpCode = code,
            userId = userId
        )

        val actual = assertDoesNotThrow {
            verifyOtpService.execute(request)
        }

        assertEquals(OtpVerificationStatus.FAILED_VERIFICATION, actual)

        verifySequence {
            mockDataStore.getOtpCode(request.otpCode)
            mockDataStore.markOtpAsUsed(updatedOtpCode)
        }
    }

}