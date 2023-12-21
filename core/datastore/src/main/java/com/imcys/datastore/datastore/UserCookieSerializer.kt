package com.imcys.datastore.datastore

import androidx.datastore.core.Serializer
import com.bilias.core.datastore.UserCookie
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserCookieSerializer @Inject constructor() : Serializer<UserCookie> {
    override val defaultValue: UserCookie
        get() = UserCookie()

    override suspend fun readFrom(input: InputStream): UserCookie = UserCookie.ADAPTER.decode(input)
    override suspend fun writeTo(t: UserCookie, output: OutputStream) {
        t.encode(output)
    }
}
