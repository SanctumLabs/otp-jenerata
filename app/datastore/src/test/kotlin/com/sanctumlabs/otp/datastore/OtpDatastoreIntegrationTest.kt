package com.sanctumlabs.otp.datastore

import com.sanctumlabs.otp.core.entities.OtpCode
import com.sanctumlabs.otp.core.entities.UserId
import com.sanctumlabs.otp.core.exceptions.DatabaseException
import com.sanctumlabs.otp.core.exceptions.NotFoundException
import com.sanctumlabs.otp.testfixtures.extensions.DatabaseExtension
import com.sanctumlabs.otp.datastore.models.OtpEntity
import com.sanctumlabs.otp.datastore.models.OtpTable
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_DRIVER
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_DRIVER_CLASS
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_PASSWORD
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_URL
import com.sanctumlabs.otp.testfixtures.utils.DATABASE_USERNAME
import com.sanctumlabs.otp.testfixtures.utils.generateRandomString
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * OTP Datastore integration tests
 */
@ExtendWith(DatabaseExtension::class)
@Tag("integration")
class OtpDatastoreIntegrationTest : KoinTest {

    private val otpDatastore by inject<OtpDatastoreImpl>()
    private val otpRepository by inject<OtpRepository>()
    private lateinit var database: Database

    @BeforeEach
    fun setup() {
        database = DatabaseFactory.init(
            DatabaseParams(
                driver = DATABASE_DRIVER,
                url = DATABASE_URL,
                driverClass = DATABASE_DRIVER_CLASS,
                username = DATABASE_USERNAME,
                password = DATABASE_PASSWORD
            )
        )
    }

    @AfterEach
    fun teardown() {
        transaction(database) {
            SchemaUtils.drop(OtpTable)
        }
    }

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(
            module {
                single { OtpRepository }
                single { OtpDatastoreImpl(get()) }
            })
    }

    @Test
    fun `context is created for the test`() {
        assertNotNull(otpRepository)
        assertNotNull(otpDatastore)
    }

    @Test
    fun `should create an OTP record in the database`() {
        val code = generateRandomString(6)
        val user = generateRandomString(10)
        val userId = UserId(user)
        val expiryTime = LocalDateTime(2023, 1, 1, 1, 1)
        val used = false
        val otpCode = OtpCode(code = code, userId = userId, expiryTime = expiryTime, used = used)

        runBlocking {
            assertDoesNotThrow {
                otpDatastore.create(otpCode)
            }
        }

        // check that we actually created a record
        val actual = transaction { OtpEntity.find { OtpTable.code eq code }.firstOrNull() }

        assertNotNull(actual)
        assertEquals(actual.code, code)
        assertEquals(actual.used, used)
        assertEquals(actual.userId, userId.value)
        assertEquals(actual.expiryTime, expiryTime.toJavaLocalDateTime())
    }

    @Test
    fun `should throw exception when attempting to create a duplicate OTP record in the database`() {
        val code = generateRandomString(6)
        val user = generateRandomString(10)
        val userId = UserId(user)
        val expiryTime = LocalDateTime(2023, 1, 1, 1, 1)
        val used = false
        val otpCode = OtpCode(code = code, userId = userId, expiryTime = expiryTime, used = used)

        // insert an existing record
        val savedOtpRecord = runBlocking {
            assertDoesNotThrow {
                otpRepository.insert(otpCode)
            }
        }
        assertNotNull(savedOtpRecord)
        assertEquals(savedOtpRecord.code, code)
        assertEquals(savedOtpRecord.used, used)
        assertEquals(savedOtpRecord.userId, userId.value)
        assertEquals(savedOtpRecord.expiryTime, expiryTime.toJavaLocalDateTime())

        runBlocking {
            assertThrows<DatabaseException> {
                otpDatastore.create(otpCode)
            }
        }
    }

    @Test
    fun `should be able to mark an existing OTP record as used`() {
        val code = generateRandomString(6)
        val user = generateRandomString(12)
        val userId = UserId(user)
        val expiryTime = LocalDateTime(2023, 1, 1, 1, 1)
        val used = false
        val otpCode = OtpCode(code = code, userId = userId, expiryTime = expiryTime, used = used)

        // insert an existing record
        val savedOtpRecord = runBlocking {
            assertDoesNotThrow {
                otpRepository.insert(otpCode)
            }
        }
        assertNotNull(savedOtpRecord)
        assertEquals(savedOtpRecord.code, code)
        assertEquals(savedOtpRecord.used, used)
        assertEquals(savedOtpRecord.userId, userId.value)
        assertEquals(savedOtpRecord.expiryTime, expiryTime.toJavaLocalDateTime())

        // mark the OTP as used
        runBlocking {
            assertDoesNotThrow {
                otpDatastore.markOtpAsUsed(otpCode)
            }
        }

        // check that we actually updated the OTP record
        val actual = transaction { OtpEntity.find { OtpTable.code eq code }.firstOrNull() }

        assertNotNull(actual)
        assertEquals(actual.code, code)
        assertEquals(actual.used, true)
        assertEquals(actual.userId, userId.value)
        assertEquals(actual.expiryTime, expiryTime.toJavaLocalDateTime())
    }

    @Test
    fun `should throw an exception when attempting to mark a non existent OTP code as used`() {
        val code = generateRandomString(11)
        val user = generateRandomString(12)
        val userId = UserId(user)
        val expiryTime = LocalDateTime(2023, 1, 1, 1, 1)
        val used = false
        val otpCode = OtpCode(code = code, userId = userId, expiryTime = expiryTime, used = used)

        runBlocking {
            assertThrows<NotFoundException> {
                otpDatastore.markOtpAsUsed(otpCode)
            }
        }
    }

    @Test
    fun `should be able to retrieve an existing OTP record`() {
        val code = generateRandomString()
        val user = generateRandomString(5)
        val userId = UserId(user)
        val expiryTime = LocalDateTime(2023, 1, 1, 1, 1)
        val used = false
        val otpCode = OtpCode(code = code, userId = userId, expiryTime = expiryTime, used = used)

        // insert an existing record
        val savedOtpRecord = runBlocking {
            assertDoesNotThrow {
                otpRepository.insert(otpCode)
            }
        }
        assertNotNull(savedOtpRecord)
        assertEquals(savedOtpRecord.code, code)
        assertEquals(savedOtpRecord.used, used)
        assertEquals(savedOtpRecord.userId, userId.value)
        assertEquals(savedOtpRecord.expiryTime, expiryTime.toJavaLocalDateTime())

        val actual = runBlocking {
            assertDoesNotThrow {
                otpDatastore.getOtpCode(code)
            }
        }

        assertNotNull(actual)
        assertEquals(actual.code, code)
        assertEquals(actual.used, used)
        assertEquals(actual.userId, userId)
        assertEquals(actual.expiryTime, expiryTime)
    }

    @Test
    fun `should throw NotFoundException when retrieving a non-existing OTP record`() {
        val code = generateRandomString(12)

        runBlocking {
            assertThrows<NotFoundException> {
                otpDatastore.getOtpCode(code)
            }
        }
    }

    @Test
    fun `should be able to retrieve all existing OTP records`() {
        val code1 = generateRandomString()
        val code2 = generateRandomString()
        val user = generateRandomString(10)
        val userId = UserId(user)
        val expiryTime1 = LocalDateTime(2023, 1, 1, 1, 1)
        val expiryTime2 = LocalDateTime(2023, 2, 1, 1, 1)
        val used1 = true
        val used2 = false
        val otpCode1 = OtpCode(code = code1, userId = userId, expiryTime = expiryTime1, used = used1)
        val otpCode2 = OtpCode(code = code2, userId = userId, expiryTime = expiryTime2, used = used2)

        // insert record(s)
        runBlocking {
            assertDoesNotThrow {
                otpRepository.insert(otpCode1)
                otpRepository.insert(otpCode2)
            }
        }

        val actual = runBlocking {
            assertDoesNotThrow {
                otpDatastore.getAll()
            }
        }

        assertNotNull(actual)
        assertEquals(2, actual.size)

        val actualCodeMap = actual.associateBy { it.code }

        val actualOtpCode1 = actualCodeMap[code1]
        assertNotNull(actualOtpCode1)
        assertEquals(actualOtpCode1.code, code1)
        assertEquals(actualOtpCode1.used, used1)
        assertEquals(actualOtpCode1.userId, userId)
        assertEquals(actualOtpCode1.expiryTime, expiryTime1)

        val actualOtpCode2 = actualCodeMap[code2]
        assertNotNull(actualOtpCode2)
        assertEquals(actualOtpCode2.code, code2)
        assertEquals(actualOtpCode2.used, used2)
        assertEquals(actualOtpCode2.userId, userId)
        assertEquals(actualOtpCode2.expiryTime, expiryTime2)
    }

    @Test
    fun `should be able to retrieve all existing OTP records for a given user`() {
        val code1 = generateRandomString()
        val code2 = generateRandomString()
        val user1 = generateRandomString(10)
        val user2 = generateRandomString(10)
        val userId1 = UserId(user1)
        val userId2 = UserId(user2)
        val expiryTime1 = LocalDateTime(2023, 1, 1, 1, 1)
        val expiryTime2 = LocalDateTime(2023, 2, 1, 1, 1)
        val used1 = true
        val used2 = false
        val otpCode1 = OtpCode(code = code1, userId = userId1, expiryTime = expiryTime1, used = used1)
        val otpCode2 = OtpCode(code = code2, userId = userId2, expiryTime = expiryTime2, used = used2)

        // insert record(s)
        runBlocking {
            assertDoesNotThrow {
                otpRepository.insert(otpCode1)
                otpRepository.insert(otpCode2)
            }
        }

        val actualUser1 = runBlocking {
            assertDoesNotThrow {
                otpDatastore.getAllByUserId(userId1)
            }
        }

        assertNotNull(actualUser1)
        assertEquals(1, actualUser1.size)

        val actualCodeMap1 = actualUser1.associateBy { it.code }
        val actualOtpCode1 = actualCodeMap1[code1]

        assertNotNull(actualOtpCode1)
        assertEquals(actualOtpCode1.code, code1)
        assertEquals(actualOtpCode1.used, used1)
        assertEquals(actualOtpCode1.userId, userId1)
        assertEquals(actualOtpCode1.expiryTime, expiryTime1)

        val actualUser2 = runBlocking {
            assertDoesNotThrow {
                otpDatastore.getAllByUserId(userId2)
            }
        }

        assertNotNull(actualUser2)
        assertEquals(1, actualUser2.size)

        val actualCodeMap2 = actualUser2.associateBy { it.code }

        val actualOtpCode2 = actualCodeMap2[code2]

        assertNotNull(actualOtpCode2)
        assertEquals(actualOtpCode2.code, code2)
        assertEquals(actualOtpCode2.used, used2)
        assertEquals(actualOtpCode2.userId, userId2)
        assertEquals(actualOtpCode2.expiryTime, expiryTime2)

    }

}
