package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.Serializer
import com.imcys.bilibilias.core.datastore.model.AsCookieStore
import com.imcys.bilibilias.core.datastore.model.Users
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
internal class UsersSerializer @Inject constructor(
    private val protoBuf: ProtoBuf,
) : Serializer<Users> {
    override val defaultValue: Users = Users()

    override suspend fun readFrom(input: InputStream): Users =
        protoBuf.decodeFromByteArray(input.readBytes())


    override suspend fun writeTo(t: Users, output: OutputStream) {
        output.write(protoBuf.encodeToByteArray(t))
    }
}
