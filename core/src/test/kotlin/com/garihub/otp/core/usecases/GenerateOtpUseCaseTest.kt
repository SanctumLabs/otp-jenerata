package com.garihub.otp.core.usecases

import com.garihub.otp.core.gateways.datastore.DataStore
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class GenerateOtpUseCaseTest {
    private val mockDataStore = mockk<DataStore>()
    private val otpKey = "otpkey"
    private val generateOtpUseCase by lazy {
        GenerateOtpUseCase(mockDataStore, otpKey)
    }


//    @Test
//    fun `Should throw illegal argument exception when executed with null params`() {
//        val expectedErrorMsg = "Must pass in a user"
//        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
//            generateOtpUseCase.execute()
//        }
//
//        Assertions.assertEquals(expectedErrorMsg, actual.message)
//    }
//
//    @Test
//    fun `Should throw UserRegistered Exception when a user has already registered`() {
//        every {
//            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
//        } returns true
//
//        val actual = Assertions.assertThrows(UserRegisteredException::class.java) {
//            generateOtpUseCase.execute(user)
//        }
//
//        Assertions.assertEquals("User already registered", actual.message)
//    }
//
//    @Test
//    fun `Should return null when registration of user with Auth Service fails`() {
//        every {
//            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
//        } returns false
//
//        every {
//            mockAuthServiceApi.registerRiderUser(
//                firstName = user.firstName,
//                lastName = user.lastName,
//                email = user.emailAddress,
//                phoneNumber = user.phoneNumber,
//                password = user.password!!,
//                gender = user.gender
//            )
//        } returns false
//
//        val actual = generateOtpUseCase.execute(user)
//
//        Assertions.assertEquals(_root_ide_package_.com.garihub.otp.core.enums.RegistrationStatus.FAILED.name, actual)
//    }
//
//    @Test
//    fun `Should return INTERNAL ERROR when registration of user with Auth Service succeeds & fails in datastore`() {
//        every {
//            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
//        } returns false
//
//        every {
//            mockAuthServiceApi.registerRiderUser(
//                firstName = user.firstName,
//                lastName = user.lastName,
//                email = user.emailAddress,
//                phoneNumber = user.phoneNumber,
//                password = user.password!!,
//                gender = user.gender
//            )
//        } returns true
//
//        every {
//            mockDataStore.createUser(user)
//        } throws DBException("Failed to register user")
//
//        val actual = generateOtpUseCase.execute(user)
//
//        Assertions.assertEquals(
//            _root_ide_package_.com.garihub.otp.core.enums.RegistrationStatus.INTERNAL_ERROR.name,
//            actual
//        )
//    }
//
//    @Test
//    fun `Should return identifier when registration of user with Auth Service succeeds & is created in datastore`() {
//        every {
//            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
//        } returns false
//
//        every {
//            mockAuthServiceApi.registerRiderUser(
//                firstName = user.firstName,
//                lastName = user.lastName,
//                email = user.emailAddress,
//                phoneNumber = user.phoneNumber,
//                password = user.password!!,
//                gender = user.gender
//            )
//        } returns true
//
//        every {
//            mockDataStore.createUser(user)
//        } returns identifier
//
//        val actual = generateOtpUseCase.execute(user)
//
//        Assertions.assertEquals(identifier, actual)
//    }
}
