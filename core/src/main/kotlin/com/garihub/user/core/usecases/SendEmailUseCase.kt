package com.garihub.user.core.usecases

import com.garihub.user.core.exceptions.EmailException
import com.garihub.user.core.gateways.email.EmailGatewayApi
import com.garihub.user.core.interactor.UseCase

class SendEmailUseCase(private val emailGatewayApi: EmailGatewayApi) : UseCase<SendEmailUseCase.Params, Boolean>() {

    data class Params(
        val email: String,
        val subject: String,
        val message: String
    )

    override fun execute(params: Params?): Boolean {
        requireNotNull(params) { "Cannot send email without email address & message" }

        return try {
            return emailGatewayApi.sendEmail(
                emailAddress = params.email,
                subject = params.subject,
                message = params.message
            )
        } catch (ee: EmailException) {
            false
        }
    }
}
