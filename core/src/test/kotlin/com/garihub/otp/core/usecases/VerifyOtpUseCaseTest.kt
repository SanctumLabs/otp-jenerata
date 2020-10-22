package com.garihub.otp.core.usecases

import com.garihub.otp.core.gateways.datastore.DataStore
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class VerifyOtpUseCaseTest {

    private val mockDataStore = mockk<DataStore>()

    private val verifyOtpUseCase by lazy {
        VerifyOtpUseCase(mockDataStore)
    }

    private val verificationToken = "verification-token"

//    @Test
//    fun `Should throw IllegalArgumentException when executed without verification token`() {
//        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
//            verifyOtpUseCase.execute()
//        }
//
//        Assertions.assertEquals("Verification token can not be null", actual.message)
//    }
//
//    @Test
//    fun `Should return Verification Failed Status when UserVerificationException is Thrown by data store`() {
//        every {
//            mockDataStore.verifyUserVerificationToken(verificationToken)
//        } throws UserVerificationException("Can not verify user")
//
//        val actual = verifyOtpUseCase.execute(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.FAILED_VERIFICATION, actual)
//    }
//
//    @Test
//    fun `Should return User Not Found Status when NotFoundException is thrown`() {
//        every {
//            mockDataStore.verifyUserVerificationToken(verificationToken)
//        } throws NotFoundException("User not found")
//
//        val actual = verifyOtpUseCase.execute(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.USER_NOT_FOUND, actual)
//    }
//
//    @Test
//    fun `Should return Token Expired status when VerificationTokenExpiredException is thrown`() {
//        every {
//            mockDataStore.verifyUserVerificationToken(verificationToken)
//        } throws VerificationTokenExpiredException("Verification token expired")
//
//        val actual = verifyOtpUseCase.execute(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.TOKEN_EXPIRED, actual)
//    }
//
//    @Test
//    fun `Should return Authentication Failed status when AuthException is thrown`() {
//        every {
//            mockDataStore.verifyUserVerificationToken(verificationToken)
//        } throws AuthException("Failed to authorize")
//
//        val actual = verifyOtpUseCase.execute(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.AUTHENTICATION_FAILED, actual)
//    }
//
//    @Test
//    fun `Should return Failed Verification status when there is a DB exception`() {
//        every {
//            mockDataStore.verifyUserVerificationToken(verificationToken)
//        } throws DBException("DB exception")
//
//        val actual = verifyOtpUseCase.execute(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.FAILED_VERIFICATION, actual)
//    }
//
//    @Test
//    fun `Should return Failed Verification status when generic exception is thrown`() {
//        every {
//            mockDataStore.verifyUserVerificationToken(verificationToken)
//        } throws Exception("Exception")
//
//        val actual = verifyOtpUseCase.execute(verificationToken)
//
//        Assertions.assertEquals(VerificationStatus.FAILED_VERIFICATION, actual)
//    }
}
