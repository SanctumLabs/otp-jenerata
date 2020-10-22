package com.garihub.otp.database

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DataStoreImplTest {

    private val mockUserRepository = mockk<UserOtpRepository>()
    private val dataStore by lazy {
        DataStoreImpl(mockUserRepository)
    }

    private val identifier = "some-identifier"
    private val verificationToken = "verification-token"

//    private val userEntity = UserOtpEntity(
//        identifier = identifier,
//        firstName = "John",
//        lastName = "Doe",
//        gender = Gender.MALE,
//        phoneNumber = "+254700000000",
//        emailAddress = "johndoe@example.com",
//        userType = UserType.RIDER
//    )
//
//    @Test
//    fun `Should throw NotFoundException when user can't be found by verification`() {
//        every {
//            mockUserRepository.findByVerifyToken(verificationToken)
//        } returns null
//
//        val actual = Assertions.assertThrows(NotFoundException::class.java) {
//            dataStore.verifyUserVerificationToken(verificationToken)
//        }
//
//        Assertions.assertEquals("User token not found or invalid", actual.message)
//    }
//
//    @Test
//    fun `Should throw VerificationTokenExpiredException when verification token has expired`() {
//        userEntity.verifyExpiryDate = LocalDateTime.now().minusDays(1)
//
//        every {
//            mockUserRepository.findByVerifyToken(verificationToken)
//        } returns userEntity
//
//        val actual = Assertions.assertThrows(VerificationTokenExpiredException::class.java) {
//            dataStore.verifyUserVerificationToken(verificationToken)
//        }
//
//        Assertions.assertEquals("It appears this token has expired", actual.message)
//    }
//
//    @Test
//    fun `Should return email address when verification token has been verified & not expired`() {
//        userEntity.verifyExpiryDate = LocalDateTime.now().plusDays(1)
//
//        every {
//            mockUserRepository.findByVerifyToken(verificationToken)
//        } returns userEntity
//
//        every {
//            mockUserRepository.save(userEntity)
//        } returns userEntity
//
//        val actual = dataStore.verifyUserVerificationToken(verificationToken)
//
//        Assertions.assertEquals(userEntity.emailAddress, actual)
//    }
//
//    @Test
//    fun `Should throw DBException when failing to save user`() {
//        userEntity.verifyExpiryDate = LocalDateTime.now().plusDays(1)
//
//        every {
//            mockUserRepository.findByVerifyToken(verificationToken)
//        } returns userEntity
//
//        every {
//            mockUserRepository.save(userEntity)
//        } throws Exception("Failed to save DB")
//
//        val actual = Assertions.assertThrows(DBException::class.java) {
//            dataStore.verifyUserVerificationToken(verificationToken)
//        }
//
//        Assertions.assertEquals("Failed to update user", actual.message)
//    }
//
//    @Test
//    fun `Should return false when an item cant be found for identifier`() {
//        val identifier = "identifier"
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns null
//
//        val actual = dataStore.deregister(identifier)
//
//        Assertions.assertFalse(actual)
//    }
//
//    @Test
//    fun `Should throw Exception when there is a failure to delete an item for a given identifier`() {
//        val identifier = "identifier"
//        val expectedExceptionMsg = "Failed to delete user $identifier"
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns userEntity
//
//        every {
//            mockUserRepository.delete(userEntity)
//        } throws Exception("Some DB exception")
//
//        val actual =
//            Assertions.assertThrows(UserException::class.java) {
//                dataStore.deregister(identifier)
//            }
//
//        Assertions.assertEquals(expectedExceptionMsg, actual.message)
//    }
//
//    @Test
//    fun `Should return true when successfully deleting an item for a given identifier`() {
//        val identifier = "identifier"
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns userEntity
//
//        every {
//            mockUserRepository.delete(userEntity)
//        } returns Unit
//
//        val actual = dataStore.deregister(identifier)
//
//        Assertions.assertTrue(actual)
//    }
//
//    @Test
//    fun `Should return false when updating an item that cant be found for given identifier`() {
//        val identifier = "identifier"
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns null
//
//        val actual = dataStore.update(identifier, user)
//
//        Assertions.assertFalse(actual)
//    }
//
//    @Test
//    fun `Should throw Exception when there is a failure to update an item for a given identifier`() {
//        val identifier = "identifier"
//        val expectedExceptionMsg = "Failed to update user"
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns userEntity
//
//        every {
//            mockUserRepository.save(userEntity)
//        } throws Exception("Some DB exception")
//
//        val actual = Assertions.assertThrows(UserException::class.java) { dataStore.update(identifier, user) }
//
//        Assertions.assertEquals(expectedExceptionMsg, actual.message)
//    }
//
//    @Test
//    fun `Should return true when successfully updating an item for a given identifier`() {
//        val identifier = "identifier"
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns userEntity
//
//        every {
//            mockUserRepository.save(userEntity)
//        } returns userEntity
//
//        val actual = dataStore.update(identifier, user)
//
//        Assertions.assertTrue(actual)
//    }
//
//    @Test
//    fun `Should throw exception when failing to create an item`() {
//        every {
//            mockUserRepository.findByPhoneNumber(user.phoneNumber)
//        } returns userEntity
//
//        every {
//            mockUserRepository.save(userEntity)
//        } throws Exception("DB Exception")
//
//        Assertions.assertThrows(UserRegisteredException::class.java) { dataStore.createUser(user) }
//    }
//
//    @Test
//    fun `Should return an identifier when an item is successfully created`() {
//        val name = "${user.firstName}:${user.lastName}"
//        mockkStatic("com.garihub.user.core.utils.UtilsKt")
//        every {
//            generateIdentifier("$name:${user.emailAddress}:${user.phoneNumber}")
//        } returns identifier
//
//        every {
//            mockUserRepository.save(userEntity)
//        } returns userEntity
//
//        every {
//            mockUserRepository.findByPhoneNumber(user.phoneNumber)
//        } returns null
//
//        val actual = dataStore.createUser(user)
//
//        Assertions.assertEquals(identifier, actual)
//    }
//
//    @Test
//    fun `Should throw exception when a user already exists with a given phone number when creating them`() {
//        val name = "${user.firstName}:${user.lastName}"
//        mockkStatic("com.garihub.user.core.utils.UtilsKt")
//        every {
//            generateIdentifier("$name:${user.emailAddress}:${user.phoneNumber}")
//        } returns identifier
//
//        every {
//            mockUserRepository.findByPhoneNumber(user.phoneNumber)
//        } returns userEntity
//
//        Assertions.assertThrows(UserRegisteredException::class.java) { dataStore.createUser(user) }
//    }
//
//    @Test
//    fun `Should return null if an item can't be found by the given identifier`() {
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns null
//
//        val actual = dataStore.getItem(identifier)
//
//        Assertions.assertNull(actual)
//    }
//
//    @Test
//    fun `Should return item for the given identifier`() {
//        every {
//            mockUserRepository.findByIdentifier(identifier)
//        } returns userEntity
//
//        val actual = dataStore.getItem(identifier)
//
//        Assertions.assertEquals(user.identifier, actual?.identifier)
//    }
}
