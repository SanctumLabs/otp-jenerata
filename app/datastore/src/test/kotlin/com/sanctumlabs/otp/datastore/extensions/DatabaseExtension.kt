package com.sanctumlabs.otp.datastore.extensions

import com.sanctumlabs.otp.datastore.utils.TestDatabaseContainer
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * A Database Extension that sets up and tears down a database before all tests in a class and after all tests
 */
class DatabaseExtension : BeforeAllCallback, AfterAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        val database = TestDatabaseContainer.init()
        database.start()
    }

    override fun afterAll(context: ExtensionContext) {
        // do nothing
    }
}
