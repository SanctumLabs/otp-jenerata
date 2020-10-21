package com.garihub.user.core.usecases

import com.garihub.user.core.gateways.datastore.DataStore
import com.garihub.user.core.exceptions.UserException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class DeleteUserUseCaseTest {
    private val mockEventDataStore = mockk<DataStore>()
    private val deleteAnEventUseCase by lazy {
        DeleteUserUseCase(mockEventDataStore)
    }

    @Test
    fun `Should throw illegal argument exception when no identifier is passed`() {
        val expectedErr = "Must pass in valid identifier"
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            deleteAnEventUseCase.execute()
        }
        Assertions.assertEquals(expectedErr, actual.message)
    }

    @Test
    fun `Should return true boolean when successful deletion occurs`() {
        val identifier = "identifier"
        every {
            mockEventDataStore.deregister(identifier)
        } returns true

        val actual = deleteAnEventUseCase.execute(identifier)

        Assertions.assertTrue(actual)
    }

    @Test
    fun `Should return false when failure to delete occurs`() {
        val identifier = "identifier"
        every {
            mockEventDataStore.deregister(identifier)
        } returns false

        val actual = deleteAnEventUseCase.execute(identifier)

        Assertions.assertFalse(actual)
    }

    @Test
    fun `Should return false when an exception is thrown by data store`() {
        val identifier = "identifier"
        every {
            mockEventDataStore.deregister(identifier)
        } throws UserException("Failed to delete user")

        val actual = deleteAnEventUseCase.execute(identifier)

        Assertions.assertFalse(actual)
    }
}
