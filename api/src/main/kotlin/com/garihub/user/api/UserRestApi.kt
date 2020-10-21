package com.garihub.user.api

import com.garihub.user.api.dto.UserDto
import com.garihub.user.core.enums.RegistrationStatus
import com.garihub.user.core.enums.VerificationStatus
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Collections

@RestController
@RequestMapping("/api/user-service/v1")
class UserRestApi(private val userService: UserService) {

    @PostMapping("/register", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun registerUser(@Validated @RequestBody payload: UserDto): ResponseEntity<Map<String, String>> {
        val result = userService.registerRider(payload) ?: return ResponseEntity(
            Collections.singletonMap("Error", "failed to register user"),
            HttpStatus.NOT_ACCEPTABLE
        )
        return when (result) {
            RegistrationStatus.FAILED.name -> ResponseEntity(
                Collections.singletonMap("message", result), HttpStatus.BAD_REQUEST
            )
            RegistrationStatus.USER_EXISTS.name -> ResponseEntity(
                Collections.singletonMap("message", result), HttpStatus.CONFLICT
            )
            RegistrationStatus.INTERNAL_ERROR.name -> ResponseEntity(
                Collections.singletonMap("message", result), HttpStatus.INTERNAL_SERVER_ERROR
            )
            RegistrationStatus.REGISTERED.name -> ResponseEntity(
                Collections.singletonMap("message", result), HttpStatus.CREATED
            )
            else -> ResponseEntity(Collections.singletonMap("identifier", result), HttpStatus.CREATED)
        }
    }

    /**
     * TODO: Redirect to a web page to show the various verification statuses for a user rather than a JSON response
     */
    @GetMapping("/verify/{token}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun verifyAccount(@PathVariable token: String): ResponseEntity<Map<String, String>> {
        return when (userService.verifyAccount(token)) {
            VerificationStatus.VERIFIED -> ResponseEntity(
                Collections.singletonMap("message", "Successfully Verified"),
                HttpStatus.OK
            )
            VerificationStatus.USER_NOT_FOUND -> ResponseEntity(
                Collections.singletonMap(
                    "message",
                    "User not found or token expired"
                ), HttpStatus.NOT_FOUND
            )
            VerificationStatus.TOKEN_EXPIRED -> ResponseEntity(
                Collections.singletonMap(
                    "message",
                    "Expired Token"
                ), HttpStatus.BAD_REQUEST
            )
            VerificationStatus.FAILED_VERIFICATION -> ResponseEntity(
                Collections.singletonMap(
                    "message",
                    "Failed to verify token"
                ), HttpStatus.INTERNAL_SERVER_ERROR
            )
            VerificationStatus.AUTHENTICATION_FAILED -> ResponseEntity(
                Collections.singletonMap(
                    "message",
                    "Unauthorized"
                ), HttpStatus.UNAUTHORIZED
            )
        }
    }

    @GetMapping("/{identifier}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUser(@PathVariable identifier: String): ResponseEntity<UserDto> {
        val item = userService.getItem(identifier) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(item, HttpStatus.OK)
    }

    @PatchMapping("/{identifier}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@PathVariable identifier: String, userDto: UserDto): ResponseEntity<Map<String, Boolean>> {
        val result = userService.update(identifier, userDto)
        return ResponseEntity(
            Collections.singletonMap("success", result),
            if (result) HttpStatus.ACCEPTED else HttpStatus.NOT_ACCEPTABLE
        )
    }

    @DeleteMapping("/{identifier}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deregisterUser(@PathVariable identifier: String): ResponseEntity<Map<String, Boolean>> {
        val result = userService.deregister(identifier)
        return ResponseEntity(
            Collections.singletonMap("success", result),
            if (result) HttpStatus.ACCEPTED else HttpStatus.NOT_ACCEPTABLE
        )
    }
}
