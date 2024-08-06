package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
    override val defaultValue = UserPreferences(
        autoMerge = true,
        shouldAppcenter = true,
    )

    override suspend fun readFrom(input: InputStream): UserPreferences = UserPreferences.ADAPTER.decode(input)

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.encode(output)
    }
}
