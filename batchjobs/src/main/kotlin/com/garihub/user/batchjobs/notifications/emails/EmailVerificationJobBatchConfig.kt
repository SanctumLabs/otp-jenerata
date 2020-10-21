package com.garihub.user.batchjobs.notifications.emails

import com.garihub.user.core.usecases.SendEmailUseCase
import com.garihub.user.database.user.UserEntity
import com.garihub.user.database.user.UserRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.domain.Sort

@Configuration
class EmailVerificationJobBatchConfig(
    @Value("\${verifyEndpoint}")
    private val verifyEndpoint: String,
    @Value("\${testEmail}")
    private val testEmail: String
) {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    companion object {
        private const val PAGE_SIZE = 10
        private const val CHUNK_SIZE = 5
    }

    @Bean("sendEmailItemReader")
    fun sendEmailItemReader(
        userRepository: UserRepository
    ): RepositoryItemReader<UserEntity> {
        val reader = RepositoryItemReader<UserEntity>()
        reader.setRepository(userRepository)
        reader.setSort(mapOf(Pair("id", Sort.Direction.ASC)))
        reader.setPageSize(PAGE_SIZE)
        reader.setMethodName("findAllByEmailSentIsFalse")
        return reader
    }

    @Bean("sendEmailProcessor")
    fun sendEmailProcessor(
        sendEmailUseCase: SendEmailUseCase,
        repository: UserRepository,
        environment: Environment
    ): EmailVerificationJobItemProcessor {
        return EmailVerificationJobItemProcessor(
            sendEmailUseCase = sendEmailUseCase,
            userRepository = repository,
            environment = environment,
            verifyEndpoint = verifyEndpoint,
            testQaEmail = testEmail
        )
    }

    @Bean("sendEmailItemWriter")
    fun sendEmailItemWriter(
        userRepository: UserRepository
    ): RepositoryItemWriter<UserEntity> {
        val repositoryItemWriter = RepositoryItemWriter<UserEntity>()
        repositoryItemWriter.setRepository(userRepository)
        repositoryItemWriter.setMethodName("save")
        return repositoryItemWriter
    }

    // / JOB config
    @Bean
    fun sendVerificationEmailJob(
        listener: EmailVerificationJobListener,
        sendEmailStep: Step
    ): Job {
        return jobBuilderFactory.get("sendVerificationEmailJob")
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(sendEmailStep)
            .end()
            .build()
    }

    @Bean
    fun sendEmailStep(
        @Qualifier("sendEmailItemReader")
        reader: RepositoryItemReader<UserEntity>,
        @Qualifier("sendEmailItemWriter")
        writer: RepositoryItemWriter<UserEntity>,
        @Qualifier("sendEmailProcessor")
        processor: EmailVerificationJobItemProcessor
    ): Step {
        return stepBuilderFactory.get("sendEmailStep")
            .chunk<UserEntity, UserEntity>(CHUNK_SIZE)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
    }
}
