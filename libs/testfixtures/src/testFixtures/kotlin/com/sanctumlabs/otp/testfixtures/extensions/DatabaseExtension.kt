package com.sanctumlabs.otp.testfixtures.extensions

import com.sanctumlabs.otp.testfixtures.utils.TestDatabaseContainer
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * A Database Extension that sets up and tears down a database before all tests in a class and after all tests
 */
class DatabaseExtension : BeforeAllCallback, AfterAllCallback {
    private val database = TestDatabaseContainer.init()
    override fun beforeAll(context: ExtensionContext) {
        database.start()
    }

    override fun afterAll(context: ExtensionContext) {
        if (database.isRunning) {
            database.stop()
        }
    }
}
