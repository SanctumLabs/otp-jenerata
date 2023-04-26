package com.sanctumlabs.otp.di.generators

import com.sanctumlabs.otp.config.Config
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GoogleOtpGeneratorConfigImpl : GoogleOtpGeneratorConfig, KoinComponent {
    private val config: Config by inject()

    override val key: String
        get() = config.getPropertyOrThrow("generators.gbotp.key")

    override val hmacAlgorithm: String
        get() = config.getProperty("generators.gbotp.hmacAlgorithm", "SHA1")

    override val enabled: Boolean
        get() = config.getProperty("generators.gbotp.enabled", "false").toBoolean()
}
