package com.garihub.user.core.gateways.email

import com.garihub.user.core.exceptions.EmailException
import kotlin.jvm.Throws

interface EmailGatewayApi {

    /**
     * Sends ane email to intended party
     * @param emailAddress [String] Email Address
     * @param subject [String] Subject of email
     * @param message [String] Message to be embedded in the email body
     */
    @Throws(EmailException::class)
    fun sendEmail(emailAddress: String, subject: String, message: String): Boolean
}
