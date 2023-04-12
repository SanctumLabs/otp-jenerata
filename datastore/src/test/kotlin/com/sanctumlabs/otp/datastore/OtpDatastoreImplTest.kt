package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class OtpDatastoreImplTest {
    private val mockOtpRepository = mockk<OtpRepository>()
    private val otpDataStore = OtpDatastoreImpl(mockOtpRepository)

    @Test
    fun `should throw exception when there is a failure to create new OTP code`() {
        val code = "123456"
        val userId = UserId("654321")
        val expiryTime = LocalDateTime.now()

        val otpCode = OtpCode(
            code = code,
            userId = userId,
            expiryTime = expiryTime
        )

        every {
            mockOtpRepository.insert(any())
        } throws Exception("failed to insert record")

        assertThrows<DatabaseException> {
            otpDataStore.create(otpCode)
        }

        verify {
            mockOtpRepository.insert(otpCode)
        }
    }

    @Test
    fun `should return otp code on successful creation of OTP`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now()

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime
        )

        every {
            mockOtpRepository.insert(any())
        } returns mockk()

        assertDoesNotThrow {
            otpDataStore.create(otpCode)
        }

        verify {
            mockOtpRepository.insert(otpCode)
        }
    }

}
