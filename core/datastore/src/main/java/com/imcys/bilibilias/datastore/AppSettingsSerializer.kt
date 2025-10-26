package com.imcys.bilibilias.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

val Context.userAppSettingsStore: DataStore<AppSettings> by dataStore(
    fileName = "app_setting.pb",
    serializer = AppSettingsSerializer
)

/**
 * 序列化
 */
object AppSettingsSerializer : Serializer<AppSettings> {

    val appSettingsDefault = AppSettings.getDefaultInstance().toBuilder()
        .setVideoNamingRule("{p_title}")
        .setBangumiNamingRule("{episode_title}")
        .build()


    override val defaultValue: AppSettings = appSettingsDefault

    override suspend fun readFrom(input: InputStream): AppSettings {
        try {
            val parsed = AppSettings.parseFrom(input)
            return when {
                parsed.bangumiNamingRule.isBlank() -> parsed.toBuilder()
                    .setBangumiNamingRule(defaultValue.bangumiNamingRule).build()

                parsed.videoNamingRule.isBlank() -> parsed.toBuilder()
                    .setVideoNamingRule(defaultValue.videoNamingRule).build()

                else -> parsed
            }
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) = t.writeTo(output)
}