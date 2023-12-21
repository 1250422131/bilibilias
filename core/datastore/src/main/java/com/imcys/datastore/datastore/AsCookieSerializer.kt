package com.imcys.datastore.datastore

import androidx.datastore.core.Serializer
import com.bilias.core.datastore.AsCookie
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AsCookieSerializer @Inject constructor() : Serializer<AsCookie> {
    override val defaultValue: AsCookie
        get() = AsCookie()

    override suspend fun readFrom(input: InputStream): AsCookie = AsCookie.ADAPTER.decode(input)
    override suspend fun writeTo(t: AsCookie, output: OutputStream) {
        t.encode(output)
    }
}
