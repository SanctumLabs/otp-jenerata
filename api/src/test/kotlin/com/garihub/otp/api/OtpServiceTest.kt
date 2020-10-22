package com.garihub.otp.api

import com.garihub.otp.api.dto.UserOtpDto
import com.garihub.otp.core.usecases.GenerateOtpUseCase
import com.garihub.otp.core.usecases.VerifyOtpUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OtpServiceTest {

    private val mockGenerateOtpUseUseCase = mockk<GenerateOtpUseCase>()
    private val mockVerifyOtpUseCase = mockk<VerifyOtpUseCase>()

    private val userService by lazy {
        OtpService(
            mockGenerateOtpUseUseCase,
            mockVerifyOtpUseCase
        )
    }

    private val verificationToken = "verification-token"
    private val identifier = "identifier"
    private val userDto = UserOtpDto(
        phoneNumber = "+254700000000"
    )

//    @Test
//    fun `Should return VerificationStatus Verified when use case verifies user`() {
//        every {
//            mockVerifyOtpUseCase.execute(verificationToken)
//        } returns VerificationStatus.VERIFIED
//
//        val actual = userService.verifyOtp(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.VERIFIED, actual)
//    }
//
//    @Test
//    fun `Should return VerificationStatus Failed Verification when use case fails to verify user`() {
//        every {
//            mockVerifyOtpUseCase.execute(verificationToken)
//        } returns VerificationStatus.FAILED_VERIFICATION
//
//        val actual = userService.verifyOtp(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.FAILED_VERIFICATION, actual)
//    }
//
//    @Test
//    fun `Should return VerificationStatus User Not Found when use case cannot find user user`() {
//        every {
//            mockVerifyOtpUseCase.execute(verificationToken)
//        } returns VerificationStatus.USER_NOT_FOUND
//
//        val actual = userService.verifyOtp(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.USER_NOT_FOUND, actual)
//    }
//
//    @Test
//    fun `Should return VerificationStatus Token Expired when use case finds user token, but it's expired`() {
//        every {
//            mockVerifyOtpUseCase.execute(verificationToken)
//        } returns VerificationStatus.TOKEN_EXPIRED
//
//        val actual = userService.verifyOtp(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.TOKEN_EXPIRED, actual)
//    }
//
//    @Test
//    fun `Should return VerificationStatus Authentication Failed when use case fails to authenticate`() {
//        every {
//            mockVerifyOtpUseCase.execute(verificationToken)
//        } returns VerificationStatus.AUTHENTICATION_FAILED
//
//        val actual = userService.verifyOtp(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.AUTHENTICATION_FAILED, actual)
//    }
//
//    @Test
//    fun `Should return a DTO when executed use case returns an item found by given identifier`() {
//        userDto.identifier = identifier
//        userDto.password = null
//        every {
//            mockGetUserUseCase.execute(identifier)
//        } returns user
//
//        val actual = userService.getItem(identifier)
//
//        Assertions.assertEquals(userDto, actual)
//    }
//
//    @Test
//    fun `Should execute use case to create a new event & return identifier`() {
//        every {
//            mockGenerateOtpUseUseCase.execute(user)
//        } returns identifier
//
//        val actual = userService.generateOtp(userDto)
//
//        Assertions.assertEquals(identifier, actual)
//    }
//
//    @Test
//    fun `Should return a boolean value when executing use case to update an item`() {
//        every {
//            mockUpdateUserUseCase.execute(UpdateUserUseCase.Params(identifier, user))
//        } returns true
//
//        val actual = userService.update(identifier, userDto)
//
//        Assertions.assertTrue(actual)
//    }
//
//    @Test
//    fun `Should return a boolean value when executing use case to delete an item`() {
//        every {
//            mockDeleteUserUseCase.execute(identifier)
//        } returns true
//
//        val actual = userService.deregister(identifier)
//
//        Assertions.assertTrue(actual)
//    }
}
