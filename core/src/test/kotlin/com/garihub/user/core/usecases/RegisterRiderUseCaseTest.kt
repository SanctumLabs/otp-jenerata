package com.garihub.user.core.usecases

import com.garihub.user.core.enums.RegistrationStatus
import com.garihub.user.core.exceptions.DBException
import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.models.Gender
import com.garihub.user.core.models.User
import com.garihub.user.core.models.UserType
import com.garihub.user.core.exceptions.UserRegisteredException
import com.garihub.user.core.gateways.auth.AuthServiceApi
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class RegisterRiderUseCaseTest {
    private val mockDataStore = mockk<DataStore>()
    private val mockAuthServiceApi = mockk<AuthServiceApi>()
    private val createEventUseCase by lazy {
        RegisterRiderUseCase(mockDataStore, mockAuthServiceApi)
    }

    private val identifier = "identifier"

    private val user = User(
        firstName = "John",
        lastName = "Doe",
        gender = Gender.MALE,
        phoneNumber = "+254700000000",
        emailAddress = "johndoe@example.com",
        userType = UserType.RIDER,
        password = "some-password"
    )

    @Test
    fun `Should throw illegal argument exception when executed with null params`() {
        val expectedErrorMsg = "Must pass in a user"
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            createEventUseCase.execute()
        }

        Assertions.assertEquals(expectedErrorMsg, actual.message)
    }

    @Test
    fun `Should throw UserRegistered Exception when a user has already registered`() {
        every {
            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
        } returns true

        val actual = Assertions.assertThrows(UserRegisteredException::class.java) {
            createEventUseCase.execute(user)
        }

        Assertions.assertEquals("User already registered", actual.message)
    }

    @Test
    fun `Should return null when registration of user with Auth Service fails`() {
        every {
            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
        } returns false

        every {
            mockAuthServiceApi.registerRiderUser(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.emailAddress,
                phoneNumber = user.phoneNumber,
                password = user.password!!,
                gender = user.gender
            )
        } returns false

        val actual = createEventUseCase.execute(user)

        Assertions.assertEquals(RegistrationStatus.FAILED.name, actual)
    }

    @Test
    fun `Should return INTERNAL ERROR when registration of user with Auth Service succeeds & fails in datastore`() {
        every {
            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
        } returns false

        every {
            mockAuthServiceApi.registerRiderUser(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.emailAddress,
                phoneNumber = user.phoneNumber,
                password = user.password!!,
                gender = user.gender
            )
        } returns true

        every {
            mockDataStore.createUser(user)
        } throws DBException("Failed to register user")

        val actual = createEventUseCase.execute(user)

        Assertions.assertEquals(RegistrationStatus.INTERNAL_ERROR.name, actual)
    }

    @Test
    fun `Should return identifier when registration of user with Auth Service succeeds & is created in datastore`() {
        every {
            mockAuthServiceApi.isRiderUserRegistered(user.emailAddress)
        } returns false

        every {
            mockAuthServiceApi.registerRiderUser(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.emailAddress,
                phoneNumber = user.phoneNumber,
                password = user.password!!,
                gender = user.gender
            )
        } returns true

        every {
            mockDataStore.createUser(user)
        } returns identifier

        val actual = createEventUseCase.execute(user)

        Assertions.assertEquals(identifier, actual)
    }
}
