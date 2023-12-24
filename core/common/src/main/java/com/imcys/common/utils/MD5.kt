package com.imcys.common.utils

import java.math.BigInteger
import java.security.MessageDigest

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    return md5Hash(input)
}

fun md5Hash(str: String): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}
