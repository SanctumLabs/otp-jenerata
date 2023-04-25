package com.sanctumlabs.otp.domain.gbotp

import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.ports.OtpDataStore
import com.sanctumlabs.otp.core.services.OtpCodeGenerator
import com.sanctumlabs.otp.datastore.OtpDatastoreImpl
import com.sanctumlabs.otp.datastore.OtpRepository
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import com.sanctumlabs.otp.domain.BaseIntegrationTest
import com.sanctumlabs.otp.domain.generators.GoogleCodeGenerator
import com.sanctumlabs.otp.domain.services.CreateOtpServiceImpl
import com.sanctumlabs.otp.testfixtures.utils.generateRandomString
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Tag("integration")
class CreateOtpServiceIntegrationTest : BaseIntegrationTest(), KoinTest {
    private val key = generateRandomString()
    private val codeDigits = 6
    private val otpDatastore by inject<OtpDataStore>()
    private val otpRepository by inject<OtpRepository>()
    private val otpGenerator by inject<OtpCodeGenerator>()
    private val createOtpService by inject<CreateOtpServiceImpl>()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(
            module {
                single { OtpRepository }
                single<OtpDataStore> { OtpDatastoreImpl(get()) }
                single<OtpCodeGenerator> { GoogleCodeGenerator(key) }
                single { CreateOtpServiceImpl(get(), get()) }
            })
    }

    @Test
    fun `context is created for the test`() {
        assertNotNull(otpRepository)
        assertNotNull(otpDatastore)
        assertNotNull(otpGenerator)
        assertNotNull(createOtpService)
    }

    @Test
    fun `should successfully generate otp code for user id`() {
        val user = generateRandomString()
        val userId = UserId(user)

        val otpCode = assertDoesNotThrow {
            runBlocking {
                createOtpService.execute(userId)
            }
        }

        val actual = transaction { OtpEntity.find { OtpTable.userId eq user }.firstOrNull() }

        assertNotNull(actual)
        assertEquals(actual.code.length, codeDigits)
        assertEquals(actual.used, false)
        assertEquals(actual.userId, userId.value)

        assertEquals(otpCode.code.length, codeDigits)
        assertEquals(otpCode.used, actual.used)
        assertEquals(otpCode.userId, userId)
        assertEquals(otpCode.expiryTime, actual.expiryTime.toKotlinLocalDateTime())
    }
}
