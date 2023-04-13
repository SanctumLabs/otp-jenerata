package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import com.sanctumlabs.otp.datastore.models.OtpEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
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

        confirmVerified(mockOtpRepository)
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

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        every {
            mockOtpRepository.insert(any())
        } returns mockOtpEntity

        assertDoesNotThrow {
            otpDataStore.create(otpCode)
        }

        verify {
            mockOtpRepository.insert(otpCode)
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should throw NotFoundException when marking otp code as used if it does not exist`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now()

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime
        )

        every {
            mockOtpRepository.findByCode(any())
        } returns null

        assertThrows<NotFoundException> {
            otpDataStore.markOtpAsUsed(otpCode)
        }

        verify {
            mockOtpRepository.findByCode(generatedCode)
        }

        verify(exactly = 0) {
            mockOtpRepository.update(any())
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should throw DatabaseException when there is a failure marking otp code as used`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now()
        val used = true

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime,
            used = used
        )

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        every {
            mockOtpRepository.findByCode(any())
        } returns mockOtpEntity

        every {
            mockOtpRepository.update(mockOtpEntity)
        } throws Exception("Failed to update otp")

        assertThrows<DatabaseException> {
            otpDataStore.markOtpAsUsed(otpCode)
        }

        verifySequence {
            mockOtpRepository.findByCode(generatedCode)
            mockOtpRepository.update(any())
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should not throw DatabaseException when there is a success marking otp code as used`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now()
        val used = true

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime,
            used = used
        )

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        every {
            mockOtpRepository.findByCode(any())
        } returns mockOtpEntity

        every {
            mockOtpRepository.update(mockOtpEntity)
        } returns 1

        assertDoesNotThrow {
            otpDataStore.markOtpAsUsed(otpCode)
        }

        verifySequence {
            mockOtpRepository.findByCode(generatedCode)
            mockOtpRepository.update(any())
        }

        confirmVerified(mockOtpRepository)
    }
}
