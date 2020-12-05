package com.garihub.otp.api

import com.garihub.otp.api.dto.UserOtpDto
import com.garihub.otp.api.dto.UserOtpResponseDto
import com.garihub.otp.api.dto.UserVerifyOtpDto
import com.garihub.otp.core.enums.OtpVerificationStatus
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.Collections

@RestController
@RequestMapping("/api/otp-service/v1")
class OtpRestApi(private val otpService: OtpService) {

    @PostMapping("/generate", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun generateOtp(@Validated @RequestBody payload: UserOtpDto): ResponseEntity<UserOtpResponseDto> {
        val result = otpService.generateOtp(payload)

        return if (result != null) {
            ResponseEntity(
                UserOtpResponseDto(
                    phoneNumberOrEmail = payload.phoneNumber,
                    otpCode = result
                ),
                HttpStatus.OK
            )
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to generate OTP code for user")
        }
    }

    @PostMapping("/verify", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun verifyOtp(@Validated @RequestBody userVerifyOtp: UserVerifyOtpDto): ResponseEntity<Map<String, String>> {
        return when (otpService.verifyOtp(userVerifyOtp)) {
            OtpVerificationStatus.VERIFIED -> ResponseEntity(
                Collections.singletonMap("message", "Successfully Verified"),
                HttpStatus.OK
            )
            OtpVerificationStatus.FAILED_VERIFICATION -> ResponseEntity(
                Collections.singletonMap(
                    "message",
                    "Failed to verify otp"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
            OtpVerificationStatus.CODE_EXPIRED -> ResponseEntity(
                Collections.singletonMap(
                    "message",
                    "Expired OTP"
                ), HttpStatus.BAD_REQUEST
            )
            OtpVerificationStatus.USER_NOT_FOUND -> ResponseEntity(
                Collections.singletonMap(
                    "message",
                    "User not found or token expired"
                ), HttpStatus.NOT_FOUND
            )
        }
    }
}
