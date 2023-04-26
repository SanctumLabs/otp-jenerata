package com.sanctumlabs.otp.di.generators

interface GeneratorConfig {
    val key: String
    val hmacAlgorithm: String
    val enabled: Boolean
}

interface TimeOtpGeneratorConfig : GeneratorConfig {
    val codeDigits: Int
    val timeStep: Long
    val timeStepUnit: String
}

interface HmacOtpGeneratorConfig : GeneratorConfig {
    val codeDigits: Int
}

interface GoogleOtpGeneratorConfig : GeneratorConfig
