package com.garihub.user.api

import com.garihub.user.api.dto.UserDto
import com.garihub.user.core.models.Gender
import com.garihub.user.core.models.UserType
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.Collections

class UserRestApiTest {

    private val mockUserService = mockk<UserService>()
    private var userRestApi: UserRestApi = UserRestApi(mockUserService)

    private val identifier = "identifier"

    private val userDto = UserDto(
        firstName = "John",
        lastName = "Doe",
        gender = Gender.MALE,
        phoneNumber = "+254700000000",
        emailAddress = "johndoe@example.com",
        userType = UserType.RIDER,
        password = "some-password"
    )

    @Test
    fun `Should return a user when user service returns an user item`() {
        every {
            mockUserService.getItem(identifier)
        } returns userDto

        val expectedResponse = ResponseEntity(userDto, HttpStatus.OK)

        val actual = userRestApi.getUser(identifier)
        Assertions.assertEquals(expectedResponse, actual)
    }

    @Test
    fun `Should return identifier when user has been created successfully by user service `() {
        every {
            mockUserService.registerRider(userDto)
        } returns identifier

        val expectedResponse =
            ResponseEntity(Collections.singletonMap("identifier", identifier), HttpStatus.CREATED)

        val actual = userRestApi.registerUser(userDto)
        Assertions.assertEquals(expectedResponse, actual)
    }

    @Test
    fun `Should return error when failed to create a user`() {
        every {
            mockUserService.registerRider(userDto)
        } returns null

        val expectedResponse =
            ResponseEntity(
                Collections.singletonMap("Error", "failed to register user"),
                HttpStatus.NOT_ACCEPTABLE
            )

        val actual = userRestApi.registerUser(userDto)
        Assertions.assertEquals(expectedResponse, actual)
    }
}
