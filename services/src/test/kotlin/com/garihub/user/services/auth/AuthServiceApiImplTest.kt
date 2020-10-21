package com.garihub.user.services.auth

import org.springframework.web.client.RestTemplate

@Suppress("ForbiddenComment")
// TODO: AuthService unit tests
class AuthServiceApiImplTest {
    private val authRestTemplate = RestTemplate()
    private val authUrl = "http://some-authurl.com"
    private val riderRealm = "garihub-rider"
    private val driverRealm = "garihub-driver"

    private val authServiceApi = AuthServiceApiImpl(authRestTemplate, authUrl, riderRealm, driverRealm)
}
