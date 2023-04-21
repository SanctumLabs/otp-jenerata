package com.sanctumlabs.otp.datastore.utils

import org.testcontainers.containers.PostgreSQLContainer

/**
 * TestDatabase is a wrapper around a test database container that is used to run integration tests.
 * This as of now sets up a PostgreSQL database with a test user and password.
 */
object TestDatabaseContainer {
    private lateinit var postgresDatabaseContainer: PostgreSQLContainer<*>

    /**
     * Initializes a database container for testing.
     * @param username the username to use for the database
     * @param password the password to use for the database
     * @param database the name of the database to use
     * @param ports the ports to use for the database
     * @param reuse whether to re-use this database container
     * @param version the version of PostgreSQL to use
     */
    @Suppress("LongParameterList", "SpreadOperator")
    fun init(
        username: String = DATABASE_USERNAME,
        password: String = DATABASE_PASSWORD,
        database: String = DATABASE_NAME,
        vararg ports: Int = intArrayOf(POSTGRESQL_DATABASE_PORT),
        reuse: Boolean = true,
        version: String = POSTGRESQL_VERSION
    ): PostgreSQLContainer<*> {
        postgresDatabaseContainer = PostgreSQLContainer(version)
            .withUsername(username)
            .withPassword(password)
            .withDatabaseName(database)
            .withExposedPorts(*ports.toTypedArray())
            .withReuse(reuse)

        return postgresDatabaseContainer
    }
}
