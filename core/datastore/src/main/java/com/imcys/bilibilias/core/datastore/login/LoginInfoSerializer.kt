package com.imcys.bilibilias.core.datastore.login

import androidx.datastore.core.Serializer
import com.imcys.bilibilias.core.datastore.LoginInfo
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class LoginInfoSerializer @Inject constructor() : Serializer<LoginInfo> {
    override val defaultValue: LoginInfo = LoginInfo()

    override suspend fun readFrom(input: InputStream): LoginInfo = LoginInfo.ADAPTER.decode(input)

    override suspend fun writeTo(t: LoginInfo, output: OutputStream) {
        t.encode(output)
    }
}
