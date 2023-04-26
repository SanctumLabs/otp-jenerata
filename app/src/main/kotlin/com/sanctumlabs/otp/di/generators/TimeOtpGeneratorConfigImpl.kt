package com.sanctumlabs.otp.di.generators

import com.sanctumlabs.otp.config.Config
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TimeOtpGeneratorConfigImpl : TimeOtpGeneratorConfig, KoinComponent {
    private val config: Config by inject()

    override val codeDigits: Int
        get() = config.getProperty("generators.tbotp.codeDigits", "6").toInt()

    override val timeStep: Long
        get() = config.getProperty("generators.tbotp.timeStep", "6").toLong()

    override val timeStepUnit: String
        get() = config.getProperty("generators.tbotp.timeStepUnit", "MINUTES")

    override val key: String
        get() = config.getPropertyOrThrow("generators.tbotp.key")

    override val hmacAlgorithm: String
        get() = config.getProperty("generators.tbotp.hmacAlgorithm", "SHA1")

    override val enabled: Boolean
        get() = config.getProperty("generators.tbotp.enabled", "true").toBoolean()
}
