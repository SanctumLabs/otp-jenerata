package com.garihub.user.batchjobs.notifications.emails

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class EmailVerificationJobListener : JobExecutionListener {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Beginning sending emails job execution StartTime: ${jobExecution.startTime}")
    }

    override fun afterJob(jobExecution: JobExecution) {
        log.info(
            "Completed sending emails job execution EndTime: ${jobExecution.endTime}, " +
                    "ExitStatus: ${jobExecution.exitStatus}"
        )
    }
}
