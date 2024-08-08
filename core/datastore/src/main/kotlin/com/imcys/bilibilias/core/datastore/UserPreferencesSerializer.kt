package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import com.imcys.bilibilias.core.datastore.di.protobuf
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class UserPreferencesSerializer @Inject constructor(
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : Serializer<UserPreferences> {
    override val defaultValue = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences = try {
        protobuf.decodeFromByteArray(input.readBytes())
    } catch (e: SerializationException) {
        throw CorruptionException("Cannot read stored data", e)
    } catch (e: IllegalArgumentException) {
        throw CorruptionException("Cannot read stored data", e)
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        withContext(ioDispatcher) {
            output.write(protobuf.encodeToByteArray(t))
        }
    }
}
