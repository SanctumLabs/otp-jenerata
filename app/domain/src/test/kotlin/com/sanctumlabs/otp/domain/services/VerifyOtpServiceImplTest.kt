package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.OtpVerificationStatus
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.entities.VerifyOtpCode
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coJustRun
import io.mockk.mockk
import io.mockk.coVerifySequence
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class VerifyOtpServiceImplTest {
    private val mockDataStore = mockk<OtpDataStore>()
    private val coVerifyOtpService = VerifyOtpServiceImpl(mockDataStore)

    @Test
    fun `should throw NotFoundException if OTP can not be found`() {
        coEvery {
            mockDataStore.getOtpCode(any())
        } throws NotFoundException("OTP code not found")

        val request = VerifyOtpCode(
            otpCode = "123456",
            userId = UserId("09876")
        )

        assertThrows<NotFoundException> {
            runBlocking {
                coVerifyOtpService.execute(request)
            }
        }

        coVerify {
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

        coEvery {
            mockDataStore.getOtpCode(any())
        } returns otpCode

        coJustRun {
            mockDataStore.markOtpAsUsed(any())
        }

        val request = VerifyOtpCode(
            otpCode = code,
            userId = userId
        )

        val actual = assertDoesNotThrow {
            runBlocking {
                coVerifyOtpService.execute(request)
            }
        }

        assertEquals(OtpVerificationStatus.VERIFIED, actual)

        coVerify {
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

        coEvery {
            mockDataStore.getOtpCode(any())
        } returns otpCode

        val request = VerifyOtpCode(
            otpCode = code,
            userId = userId
        )

        val actual = assertDoesNotThrow {
            runBlocking {
                coVerifyOtpService.execute(request)
            }
        }

        assertEquals(OtpVerificationStatus.CODE_EXPIRED, actual)

        coVerify {
            mockDataStore.getOtpCode(request.otpCode)
        }

        coVerify(exactly = 0) {
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

        coEvery {
            mockDataStore.getOtpCode(any())
        } returns otpCode

        coEvery {
            mockDataStore.markOtpAsUsed(any())
        } throws Exception("Failed to mark OTP as used")

        val request = VerifyOtpCode(
            otpCode = code,
            userId = userId
        )

        val actual = assertDoesNotThrow {
            runBlocking {
                coVerifyOtpService.execute(request)
            }
        }

        assertEquals(OtpVerificationStatus.FAILED_VERIFICATION, actual)

        coVerifySequence {
            mockDataStore.getOtpCode(request.otpCode)
            mockDataStore.markOtpAsUsed(updatedOtpCode)
        }
    }
}
