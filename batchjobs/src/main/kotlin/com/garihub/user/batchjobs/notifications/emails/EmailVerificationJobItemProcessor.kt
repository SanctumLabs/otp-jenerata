package com.garihub.user.batchjobs.notifications.emails

import com.garihub.user.batchjobs.AMOUNT_TO_ADD
import com.garihub.user.batchjobs.PROFILE_UAT
import com.garihub.user.batchjobs.PROFILE_DEV
import com.garihub.user.batchjobs.PROFILE_PRODUCTION
import com.garihub.user.batchjobs.TEST_QA_EMAIL
import com.garihub.user.core.usecases.SendEmailUseCase
import com.garihub.user.database.user.UserEntity
import com.garihub.user.database.user.UserRepository
import org.springframework.batch.item.ItemProcessor
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.UUID

open class EmailVerificationJobItemProcessor(
    private val sendEmailUseCase: SendEmailUseCase,
    private val userRepository: UserRepository,
    private val environment: Environment,
    private val verifyEndpoint: String,
    private val testQaEmail: String = TEST_QA_EMAIL
) : ItemProcessor<UserEntity, UserEntity> {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @Suppress("TooGenericExceptionCaught")
    @Transactional
    override fun process(user: UserEntity): UserEntity? {
        log.info("Sending verification email to: ${user.emailAddress} for User: ${user.identifier}")

        // get current running profile
        val currentProfiles = environment.activeProfiles

        // Only send an email to the actual user if we are in dev, uat or production
        val isDevUatOrProd =
            (currentProfiles.contains(PROFILE_DEV) || currentProfiles.contains(PROFILE_UAT) || currentProfiles.contains(
                PROFILE_PRODUCTION
            ))

        val instant: Instant = Instant.now()
        val nextExpiry = instant.plus(AMOUNT_TO_ADD, ChronoUnit.DAYS)
        val verifyTokenExpiry = LocalDateTime.ofInstant(nextExpiry, ZoneOffset.UTC)
        val verifyToken = UUID.randomUUID().toString()
        val link = "$verifyEndpoint/$verifyToken"
        val message = "Follow this link $link to verify your email"

        return try {
            val result = sendEmailUseCase.execute(
                SendEmailUseCase.Params(
                    email = if (isDevUatOrProd) user.emailAddress else testQaEmail,
                    subject = "Account verification",
                    message = message
                )
            )

            if (result) {
                user.emailSent = result
                user.verifyToken = verifyToken
                user.verifyExpiryDate = verifyTokenExpiry
                userRepository.saveAndFlush(user)
            } else {
                null
            }
        } catch (e: Exception) {
            log.error("Failed to send customer email notification with message ${e.message}")
            null
        }
    }
}
