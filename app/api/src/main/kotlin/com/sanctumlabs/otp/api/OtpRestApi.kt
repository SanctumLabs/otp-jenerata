package com.sanctumlabs.otp.api

import com.sanctumlabs.otp.api.dto.ApiResult
import com.sanctumlabs.otp.api.dto.OtpRequestDto
import com.sanctumlabs.otp.api.dto.OtpResponseDto
import com.sanctumlabs.otp.api.dto.OtpVerifyDto
import com.sanctumlabs.otp.api.dto.OtpVerifyResponseDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.otpApiRoutes() {
    val otpService: OtpService by inject()

    route("/api/v1/otp") {
        post<OtpRequestDto> { payload ->
            runCatching {
                val otpCode = otpService.generateOtp(payload)
                OtpResponseDto(
                    userId = payload.userId,
                    code = otpCode.code,
                    expiryTime = otpCode.expiryTime
                )
            }
                .onSuccess { call.respond(message = it, status = HttpStatusCode.Created) }
                .onFailure {
                    call.respond(
                        message = ApiResult(message = it.message ?: "Failed to create OTP"),
                        status = HttpStatusCode.InternalServerError
                    )
                }
        }

        post<OtpVerifyDto>("/verify") { payload ->
            runCatching {
                val status = otpService.verifyOtp(payload)
                OtpVerifyResponseDto(
                    userId = payload.userId,
                    code = payload.code,
                    status = status
                )
            }
                .onSuccess { call.respond(message = it, status = HttpStatusCode.OK) }
                .onFailure {
                    call.respond(
                        message = ApiResult(message = it.message ?: "Failed to verify OTP"),
                        status = HttpStatusCode.InternalServerError
                    )
                }
        }
    }
}