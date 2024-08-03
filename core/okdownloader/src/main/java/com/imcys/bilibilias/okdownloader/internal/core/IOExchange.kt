package com.imcys.bilibilias.okdownloader.internal.core

import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.appendingSink
import okio.buffer
import java.io.File
import java.io.IOException

internal class IOExchange {
    @Throws(IOException::class)
    fun exchange(file: File, source: BufferedSource, onRead: (Long) -> Unit) {
        file.appendingSink().buffer().use {
            it.writeAll(object : ForwardingSource(source) {
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytes = super.read(sink, byteCount)
                    onRead(bytes)
                    return bytes
                }
            })
        }
    }
}
