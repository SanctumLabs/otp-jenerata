package com.garihub.otp.api

import com.garihub.otp.api.dto.UserOtpDto
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.Collections

class OtpRestApiTest {

    private val mockUserService = mockk<OtpService>()
    private var otpRestApi: OtpRestApi = OtpRestApi(mockUserService)

    private val identifier = "identifier"

    private val userDto = UserOtpDto(
        phoneNumber = "+254700000000"
    )

//    @Test
//    fun `Should return a user when user service returns an user item`() {
//        every {
//            mockUserService.getItem(identifier)
//        } returns userDto
//
//        val expectedResponse = ResponseEntity(userDto, HttpStatus.OK)
//
//        val actual = otpRestApi.getUser(identifier)
//        Assertions.assertEquals(expectedResponse, actual)
//    }
//
//    @Test
//    fun `Should return identifier when user has been created successfully by user service `() {
//        every {
//            mockUserService.generateOtp(userDto)
//        } returns identifier
//
//        val expectedResponse =
//            ResponseEntity(Collections.singletonMap("identifier", identifier), HttpStatus.CREATED)
//
//        val actual = otpRestApi.generateOtp(userDto)
//        Assertions.assertEquals(expectedResponse, actual)
//    }
//
//    @Test
//    fun `Should return error when failed to create a user`() {
//        every {
//            mockUserService.generateOtp(userDto)
//        } returns null
//
//        val expectedResponse =
//            ResponseEntity(
//                Collections.singletonMap("Error", "failed to register user"),
//                HttpStatus.NOT_ACCEPTABLE
//            )
//
//        val actual = otpRestApi.generateOtp(userDto)
//        Assertions.assertEquals(expectedResponse, actual)
//    }
}
