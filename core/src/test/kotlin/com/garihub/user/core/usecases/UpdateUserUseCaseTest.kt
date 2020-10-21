package com.garihub.user.core.usecases

import com.garihub.user.core.exceptions.UserException
import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.models.Gender
import com.garihub.user.core.models.User
import com.garihub.user.core.models.UserType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UpdateUserUseCaseTest {
    private val mockDataStore = mockk<DataStore>()
    private val updateUserUseCase by lazy {
        UpdateUserUseCase(mockDataStore)
    }
    private val identifier = "some-identifier"
    private val user = User(
            identifier = identifier,
            firstName = "John",
            lastName = "Doe",
            gender = Gender.MALE,
            phoneNumber = "+254700000000",
            emailAddress = "johndoe@example.com",
            userType = UserType.RIDER,
            password = "some-password"
    )

    @Test
    fun `Should throw illegal argument exception when null params are passed`() {
        val expectedErr = "Must pass in valid params to update an event"
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            updateUserUseCase.execute()
        }
        assertEquals(expectedErr, actual.message)
    }

    @Test
    fun `Should return false when there is an exception updating a user`() {
        every {
            mockDataStore.update(identifier, user)
        } throws UserException("DB Exception")

        val actual = updateUserUseCase.execute(UpdateUserUseCase.Params(identifier, user))

        assertFalse(actual)
    }

    @Test
    fun `Should return true when successful update of a user occurs`() {
        every {
            mockDataStore.update(identifier, user)
        } returns true

        val actual = updateUserUseCase.execute(UpdateUserUseCase.Params(identifier, user))

        assertTrue(actual)
    }
}
