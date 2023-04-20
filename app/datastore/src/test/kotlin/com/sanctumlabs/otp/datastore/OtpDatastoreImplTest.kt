package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import com.sanctumlabs.otp.datastore.models.OtpEntity
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.coVerify
import io.mockk.coVerifySequence
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class OtpDatastoreImplTest {
    private val mockOtpRepository = mockk<OtpRepository>()
    private val otpDataStore = OtpDatastoreImpl(mockOtpRepository)

    @Test
    fun `should throw exception when there is a failure to create new OTP code`() {
        val code = "123456"
        val userId = UserId("654321")
        val expiryTime = LocalDateTime.now().toKotlinLocalDateTime()

        val otpCode = OtpCode(
            code = code,
            userId = userId,
            expiryTime = expiryTime
        )

        coEvery {
            mockOtpRepository.insert(any())
        } throws Exception("failed to insert record")

        assertThrows<DatabaseException> {
            runBlocking {
                otpDataStore.create(otpCode)
            }
        }

        coVerify {
            mockOtpRepository.insert(otpCode)
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should return otp code on successful creation of OTP`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now().toKotlinLocalDateTime()

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime
        )

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpRepository.insert(any())
        } returns mockOtpEntity

        assertDoesNotThrow {
            runBlocking {
                otpDataStore.create(otpCode)
            }
        }

        coVerify {
            mockOtpRepository.insert(otpCode)
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should throw NotFoundException when marking otp code as used if it does not exist`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now().toKotlinLocalDateTime()

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime
        )

        coEvery {
            mockOtpRepository.findByCode(any())
        } returns null

        assertThrows<NotFoundException> {
            runBlocking {
                otpDataStore.markOtpAsUsed(otpCode)
            }
        }

        coVerify {
            mockOtpRepository.findByCode(generatedCode)
        }

        coVerify(exactly = 0) {
            mockOtpRepository.update(any())
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should throw DatabaseException when there is a failure marking otp code as used`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now().toKotlinLocalDateTime()
        val used = true

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime,
            used = used
        )

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpRepository.findByCode(any())
        } returns mockOtpEntity

        coEvery {
            mockOtpRepository.update(mockOtpEntity)
        } throws Exception("Failed to update otp")

        assertThrows<DatabaseException> {
            runBlocking {
                otpDataStore.markOtpAsUsed(otpCode)
            }
        }

        coVerifySequence {
            mockOtpRepository.findByCode(generatedCode)
            mockOtpRepository.update(any())
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should not throw DatabaseException when there is a success marking otp code as used`() {
        val generatedCode = "123456"
        val otpUserId = UserId("654321")
        val otpExpiryTime = LocalDateTime.now().toKotlinLocalDateTime()
        val used = true

        val otpCode = OtpCode(
            code = generatedCode,
            userId = otpUserId,
            expiryTime = otpExpiryTime,
            used = used
        )

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpRepository.findByCode(any())
        } returns mockOtpEntity

        coEvery {
            mockOtpRepository.update(mockOtpEntity)
        } returns 1

        assertDoesNotThrow {
            runBlocking {
                otpDataStore.markOtpAsUsed(otpCode)
            }
        }

        coVerifySequence {
            mockOtpRepository.findByCode(generatedCode)
            mockOtpRepository.update(any())
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should throw NotFoundException when retrieving an OTP by code & it's non-existent`() {
        val generatedCode = "123456"

        coEvery {
            mockOtpRepository.findByCode(any())
        } returns null

        assertThrows<NotFoundException> {
            runBlocking {
                otpDataStore.getOtpCode(generatedCode)
            }
        }

        coVerify {
            mockOtpRepository.findByCode(generatedCode)
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should return OTP when retrieving it by code & it exists`() {
        val code = "123456"
        val userId = UserId("654321")
        val expiryTime = LocalDateTime.now()
        val used = false

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpEntity.code
        } returns code

        coEvery {
            mockOtpEntity.used
        } returns used

        coEvery {
            mockOtpEntity.userId
        } returns userId.value

        coEvery {
            mockOtpEntity.expiryTime
        } returns expiryTime

        coEvery {
            mockOtpRepository.findByCode(any())
        } returns mockOtpEntity

        val actual = assertDoesNotThrow {
            runBlocking {
                otpDataStore.getOtpCode(code)
            }
        }

        assertEquals(code, actual.code)
        assertEquals(used, actual.used)
        assertEquals(userId, actual.userId)
        assertEquals(expiryTime, actual.expiryTime.toJavaLocalDateTime())

        coVerify {
            mockOtpRepository.findByCode(code)
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should return Collection of OTPs when retrieving all codes`() {
        val code = "123456"
        val userId = UserId("654321")
        val expiryTime = LocalDateTime.now()
        val used = false

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpEntity.code
        } returns code

        coEvery {
            mockOtpEntity.used
        } returns used

        coEvery {
            mockOtpEntity.userId
        } returns userId.value

        coEvery {
            mockOtpEntity.expiryTime
        } returns expiryTime

        val otpCodes = listOf(mockOtpEntity)

        coEvery {
            mockOtpRepository.findAll()
        } returns otpCodes

        val actual = assertDoesNotThrow {
            runBlocking {
                otpDataStore.getAll()
            }
        }

        assertEquals(otpCodes.size, actual.size)

        coVerify {
            mockOtpRepository.findAll()
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should throw exception when there is a failure retrieving all OTP codes`() {
        val code = "123456"
        val userId = UserId("654321")
        val expiryTime = LocalDateTime.now()
        val used = false

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpEntity.code
        } returns code

        coEvery {
            mockOtpEntity.used
        } returns used

        coEvery {
            mockOtpEntity.userId
        } returns userId.value

        coEvery {
            mockOtpEntity.expiryTime
        } returns expiryTime

        coEvery {
            mockOtpRepository.findAll()
        } throws Exception("Failed to retrieve all OTP codes")

        assertThrows<DatabaseException> {
            runBlocking {
                otpDataStore.getAll()
            }
        }

        coVerify {
            mockOtpRepository.findAll()
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should throw exception when there is a failure retrieving all OTP codes by user ID`() {
        val code = "123456"
        val userId = UserId("654321")
        val expiryTime = LocalDateTime.now()
        val used = false

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpEntity.code
        } returns code

        coEvery {
            mockOtpEntity.used
        } returns used

        coEvery {
            mockOtpEntity.userId
        } returns userId.value

        coEvery {
            mockOtpEntity.expiryTime
        } returns expiryTime

        coEvery {
            mockOtpRepository.findAllByUserId(any())
        } throws Exception("Failed to retrieve all OTP codes")

        assertThrows<DatabaseException> {
            runBlocking {
                otpDataStore.getAllByUserId(userId)
            }
        }

        coVerify {
            mockOtpRepository.findAllByUserId(userId.value)
        }

        confirmVerified(mockOtpRepository)
    }

    @Test
    fun `should retrieve collection of OTP codes for a user ID`() {
        val code = "123456"
        val userId = UserId("654321")
        val expiryTime = LocalDateTime.now()
        val used = false

        val mockOtpEntity = mockk<OtpEntity>(relaxed = true)

        coEvery {
            mockOtpEntity.code
        } returns code

        coEvery {
            mockOtpEntity.used
        } returns used

        coEvery {
            mockOtpEntity.userId
        } returns userId.value

        coEvery {
            mockOtpEntity.expiryTime
        } returns expiryTime

        val otpCodes = listOf(mockOtpEntity)

        coEvery {
            mockOtpRepository.findAllByUserId(any())
        } returns otpCodes

        assertDoesNotThrow {
            runBlocking {
                otpDataStore.getAllByUserId(userId)
            }
        }

        coVerify {
            mockOtpRepository.findAllByUserId(userId.value)
        }

        confirmVerified(mockOtpRepository)
    }
}
