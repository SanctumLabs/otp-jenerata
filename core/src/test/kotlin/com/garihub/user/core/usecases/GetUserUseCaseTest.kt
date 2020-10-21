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
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class GetUserUseCaseTest {

    private val mockEventDataStore = mockk<DataStore>()
    private val getUserUseCase by lazy {
        GetUserUseCase(mockEventDataStore)
    }

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
    fun `Should throw an illegal argument exception when a null identifier is passed`() {
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            getUserUseCase.execute()
        }

        assertEquals("Must have an identifier to get an event item", actual.message)
    }

    @Test
    fun `Should return null when an Exception is thrown`() {
        every {
            mockEventDataStore.getItem("identifier")
        } throws UserException("Failed to get a user")

        val actual = getUserUseCase.execute("identifier")
        Assertions.assertNull(actual)
    }

    @Test
    fun `Should return an item, if it is found for given identifier`() {
        every {
            mockEventDataStore.getItem("identifier")
        } returns user

        val actual = getUserUseCase.execute("identifier")

        assertEquals(user, actual)
    }
}
