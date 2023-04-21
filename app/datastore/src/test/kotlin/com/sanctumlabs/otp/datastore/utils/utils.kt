package com.sanctumlabs.otp.datastore.utils

fun generateRandomString(length: Int = 6): String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}
