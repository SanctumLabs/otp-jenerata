package com.sanctumlabs.otp.di.generators

import com.sanctumlabs.otp.config.Config
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HmacOtpGeneratorConfigImpl : HmacOtpGeneratorConfig, KoinComponent {
    private val config: Config by inject()

    override val codeDigits: Int
        get() = config.getProperty("generators.hbotp.codeDigits", "6").toInt()

    override val key: String
        get() = config.getPropertyOrThrow("generators.hbotp.key")

    override val hmacAlgorithm: String
        get() = config.getProperty("generators.hbotp.hmacAlgorithm", "SHA1")

    override val enabled: Boolean
        get() = config.getProperty("generators.hbotp.enabled", "false").toBoolean()
}
