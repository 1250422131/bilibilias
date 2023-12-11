package com.imcys.common.utils

import android.content.pm.Signature
import dev.utils.app.SignaturesUtils
import java.math.BigInteger
import java.security.MessageDigest

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    return SignaturesUtils.signatureMD5(arrayOf(Signature(input)))
}