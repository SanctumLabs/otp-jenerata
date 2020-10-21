package com.garihub.user.core.utils

import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.experimental.and
import java.security.NoSuchAlgorithmException
import java.util.UUID

private const val BYTE_CODE_FF = 0xff
private const val BYTE_CODE_100 = 0x100
private const val RADIX = 16

private fun bytesToHex(hash: ByteArray): String {
    val hexString = StringBuffer()
    for (i in hash.indices) {
        val hex = Integer.toHexString(BYTE_CODE_FF and hash[i].toInt())
        if (hex.length == 1)
            hexString.append('0')
        hexString.append(hex)
    }
    return hexString.toString()
}

@Throws(NoSuchAlgorithmException::class)
fun generateMd5Hash(originalString: String): String {
    val digest = MessageDigest.getInstance("MD5")
    val encodedHash = digest.digest(originalString.toByteArray(StandardCharsets.UTF_8))
    return bytesToHex(encodedHash)
}

@Throws(NoSuchAlgorithmException::class)
fun generateSHA256(originalString: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(originalString.toByteArray())
    val bytes = md.digest()
    val sb = StringBuilder()
    for (i in bytes.indices) {
        sb.append(((bytes[i] and BYTE_CODE_FF.toByte()) + BYTE_CODE_100).toString(RADIX).substring(1))
    }
    return sb.toString()
}

fun generateIdentifier(originalStr: String): String {
    return try {
        generateMd5Hash(originalStr)
    } catch (nsae: NoSuchAlgorithmException) {
        generateSHA256(originalStr)
    } finally {
        UUID.randomUUID().toString()
    }
}
