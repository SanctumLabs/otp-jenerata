package com.garihub.user.api

import com.garihub.user.api.dto.UserDto
import com.garihub.user.core.enums.VerificationStatus
import com.garihub.user.core.models.Gender
import com.garihub.user.core.models.User
import com.garihub.user.core.models.UserType
import com.garihub.user.core.usecases.RegisterRiderUseCase
import com.garihub.user.core.usecases.DeleteUserUseCase
import com.garihub.user.core.usecases.VerifyRiderEmailAddressUseCase
import com.garihub.user.core.usecases.GetUserUseCase
import com.garihub.user.core.usecases.UpdateUserUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserServiceTest {

    private val mockCreateUserUseCase = mockk<RegisterRiderUseCase>()
    private val mockVerifyRiderEmailUseCase = mockk<VerifyRiderEmailAddressUseCase>()
    private val mockGetUserUseCase = mockk<GetUserUseCase>()
    private val mockUpdateUserUseCase = mockk<UpdateUserUseCase>()
    private val mockDeleteUserUseCase = mockk<DeleteUserUseCase>()

    private val userService by lazy {
        UserService(
            mockCreateUserUseCase,
            mockVerifyRiderEmailUseCase,
            mockGetUserUseCase,
            mockUpdateUserUseCase,
            mockDeleteUserUseCase
        )
    }

    private val verificationToken = "verification-token"
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
    fun `Should return VerificationStatus Verified when use case verifies user`() {
        every {
            mockVerifyRiderEmailUseCase.execute(verificationToken)
        } returns VerificationStatus.VERIFIED

        val actual = userService.verifyAccount(verificationToken)

        Assertions.assertEquals(VerificationStatus.VERIFIED, actual)
    }

    @Test
    fun `Should return VerificationStatus Failed Verification when use case fails to verify user`() {
        every {
            mockVerifyRiderEmailUseCase.execute(verificationToken)
        } returns VerificationStatus.FAILED_VERIFICATION

        val actual = userService.verifyAccount(verificationToken)

        Assertions.assertEquals(VerificationStatus.FAILED_VERIFICATION, actual)
    }

    @Test
    fun `Should return VerificationStatus User Not Found when use case cannot find user user`() {
        every {
            mockVerifyRiderEmailUseCase.execute(verificationToken)
        } returns VerificationStatus.USER_NOT_FOUND

        val actual = userService.verifyAccount(verificationToken)

        Assertions.assertEquals(VerificationStatus.USER_NOT_FOUND, actual)
    }

    @Test
    fun `Should return VerificationStatus Token Expired when use case finds user token, but it's expired`() {
        every {
            mockVerifyRiderEmailUseCase.execute(verificationToken)
        } returns VerificationStatus.TOKEN_EXPIRED

        val actual = userService.verifyAccount(verificationToken)

        Assertions.assertEquals(VerificationStatus.TOKEN_EXPIRED, actual)
    }

    @Test
    fun `Should return VerificationStatus Authentication Failed when use case fails to authenticate`() {
        every {
            mockVerifyRiderEmailUseCase.execute(verificationToken)
        } returns VerificationStatus.AUTHENTICATION_FAILED

        val actual = userService.verifyAccount(verificationToken)

        Assertions.assertEquals(VerificationStatus.AUTHENTICATION_FAILED, actual)
    }

    @Test
    fun `Should return a DTO when executed use case returns an item found by given identifier`() {
        userDto.identifier = identifier
        userDto.password = null
        every {
            mockGetUserUseCase.execute(identifier)
        } returns user

        val actual = userService.getItem(identifier)

        Assertions.assertEquals(userDto, actual)
    }

    @Test
    fun `Should execute use case to create a new event & return identifier`() {
        every {
            mockCreateUserUseCase.execute(user)
        } returns identifier

        val actual = userService.registerRider(userDto)

        Assertions.assertEquals(identifier, actual)
    }

    @Test
    fun `Should return a boolean value when executing use case to update an item`() {
        every {
            mockUpdateUserUseCase.execute(UpdateUserUseCase.Params(identifier, user))
        } returns true

        val actual = userService.update(identifier, userDto)

        Assertions.assertTrue(actual)
    }

    @Test
    fun `Should return a boolean value when executing use case to delete an item`() {
        every {
            mockDeleteUserUseCase.execute(identifier)
        } returns true

        val actual = userService.deregister(identifier)

        Assertions.assertTrue(actual)
    }
}
