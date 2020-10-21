package com.garihub.user.api

import com.garihub.user.api.dto.UserDto
import com.garihub.user.core.exceptions.InvalidPhoneNumberException
import com.garihub.user.core.models.User
import com.garihub.user.core.UserIdentifier
import com.garihub.user.core.enums.VerificationStatus
import com.garihub.user.core.usecases.RegisterRiderUseCase
import com.garihub.user.core.usecases.DeleteUserUseCase
import com.garihub.user.core.usecases.VerifyRiderEmailAddressUseCase
import com.garihub.user.core.usecases.GetUserUseCase
import com.garihub.user.core.usecases.UpdateUserUseCase
import com.garihub.user.core.utils.isPhoneNumberValid
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class UserService(
    private val registerRiderUseCase: RegisterRiderUseCase,
    private val verifyRiderEmailAddressUseCase: VerifyRiderEmailAddressUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) {

    /**
     * Verifies a user account
     * @return [Boolean] True if the account was successfully verified
     */
    fun verifyAccount(token: String): VerificationStatus {
        return verifyRiderEmailAddressUseCase.execute(token)
    }

    fun getItem(identifier: UserIdentifier): UserDto? {
        val item = getUserUseCase.execute(identifier) ?: return null
        return UserDto(
            identifier,
            firstName = item.firstName,
            lastName = item.lastName,
            emailAddress = item.emailAddress,
            phoneNumber = item.phoneNumber,
            gender = item.gender,
            userType = item.userType
        )
    }

    @Suppress("TooGenericExceptionCaught")
    fun registerRider(userDto: UserDto): String? {
        return try {
            if (isPhoneNumberValid(userDto.phoneNumber)) {
                val user = User(
                    firstName = userDto.firstName,
                    lastName = userDto.lastName,
                    emailAddress = userDto.emailAddress,
                    password = userDto.password,
                    phoneNumber = userDto.phoneNumber,
                    gender = userDto.gender,
                    userType = userDto.userType
                )
                registerRiderUseCase.execute(user)
            } else {
                null
            }
        } catch (e: Exception) {
            @Suppress("ForbiddenComment")
            // todo: put in proper responses per exception thrown
            when (e) {
                is InvalidPhoneNumberException -> {
                    null
                }
                else -> null
            }
        }
    }

    fun update(identifier: UserIdentifier, userDto: UserDto): Boolean {
        val user = User(
            firstName = userDto.firstName,
            lastName = userDto.lastName,
            emailAddress = userDto.emailAddress,
            password = userDto.password,
            phoneNumber = userDto.phoneNumber,
            gender = userDto.gender,
            userType = userDto.userType
        )

        return updateUserUseCase.execute(UpdateUserUseCase.Params(identifier, user))
    }

    fun deregister(identifier: UserIdentifier): Boolean {
        return deleteUserUseCase.execute(identifier)
    }
}
