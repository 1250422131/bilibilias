package com.imcys.bilibilias.core.datastore.preferences

import androidx.datastore.core.Serializer
import com.imcys.bilibilias.core.common.download.DefaultConfig
import com.imcys.bilibilias.core.datastore.UserPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

internal class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
    override val defaultValue = UserPreferences(
        fileStoragePath = DefaultConfig.defaultStorePath,
        fileNameRule = DefaultConfig.defaultNameRule,
        autoMerge = true,
        shouldAppcenter = true,
        command = DefaultConfig.defaultCommand
    )

    override suspend fun readFrom(input: InputStream): UserPreferences {
        return UserPreferences.ADAPTER.decode(input)
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.encode(output)
    }
}
