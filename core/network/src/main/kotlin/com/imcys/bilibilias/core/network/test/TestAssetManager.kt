package com.imcys.bilibilias.core.network.test

import java.io.InputStream

fun interface TestAssetManager {
    fun open(fileName: String): InputStream
}
