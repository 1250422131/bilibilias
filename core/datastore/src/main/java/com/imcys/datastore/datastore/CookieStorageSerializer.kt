package com.imcys.datastore.datastore

import androidx.datastore.core.*
import com.bilias.core.datastore.cookie.*
import java.io.*
import javax.inject.*

class CookieStorageSerializer @Inject constructor() : Serializer<CookieStorage> {
    override val defaultValue: CookieStorage
        get() = CookieStorage()

    override suspend fun readFrom(input: InputStream): CookieStorage =
        CookieStorage.ADAPTER.decode(input)

    override suspend fun writeTo(t: CookieStorage, output: OutputStream) {
        t.encode(output)
    }
}
