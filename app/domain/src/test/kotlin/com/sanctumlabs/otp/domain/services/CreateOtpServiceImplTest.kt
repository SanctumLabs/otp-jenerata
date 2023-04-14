package com.sanctumlabs.otp.domain.services

import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.services.GeneratedOtpCode
import com.sanctumlabs.otp.core.services.OtpCodeGenerator
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime


class CreateOtpServiceImplTest {
    private val mockDataStore = mockk<OtpDataStore>()
    private val mockOtpGenerator = mockk<OtpCodeGenerator>()
    private val createOtpService = CreateOtpServiceImpl(
        mockDataStore, mockOtpGenerator
    )

    @Test
    fun `should throw error if there is a failure generating code`() {
        every {
            mockOtpGenerator.generate(any())
        } throws Exception("Failed to generate OTP code")

        assertThrows<CreateOtpException> {
            createOtpService.execute(UserId("123456"))
        }

        verify {
            mockOtpGenerator.generate("123456")
            mockDataStore wasNot Called
        }
    }

    @Test
    fun `should throw error if there is a failure saving generated OTP code`() {
        val generatedCode = GeneratedOtpCode(
            code = "123456",
            expiryTime = LocalDateTime.now()
        )
        val userId = UserId("123456")

        every {
            mockOtpGenerator.generate(any())
        } returns generatedCode

        every {
            mockDataStore.create(any())
        } throws Exception("Failed to save OTP code")

        assertThrows<Exception> {
            createOtpService.execute(userId)
        }

        verifySequence {
            mockOtpGenerator.generate(userId.value)
            mockDataStore.create(any())
        }
    }
}
