package com.imcys.bilibilias.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.Settings
import com.imcys.bilibilias.datastore.copy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

class AppSettingsRepository(
    private val dataStore: DataStore<AppSettings>,
) {
    private val TAG: String = "AppSettingsRepository"

    val appSettingsFlow: Flow<AppSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(AppSettings.getDefaultInstance())
            } else {
                throw exception
            }
        }


    // 添加更新隐私政策同意状态的方法
    suspend fun updatePrivacyPolicyAgreement(agreed: AppSettings.AgreePrivacyPolicyState) {
        dataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setAgreePrivacyPolicy(agreed)
                .build()
        }
    }


    // 添加更新隐私政策同意状态的方法
    suspend fun updateKnowAboutApp(knowAboutApp: AppSettings.KnowAboutApp) {
        dataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setKnowAboutApp(knowAboutApp)
                .build()
        }
    }

    suspend fun updateRoamEnabledState(enabled: Boolean) {
        dataStore.updateData { currentSettings ->
            currentSettings.copy {
                enabledRoam = enabled
            }
        }
    }

    suspend fun updateEnabledDynamicColor(enabled: Boolean) {
        dataStore.updateData { currentSettings ->
            currentSettings.copy {
                enabledDynamicColor = enabled
            }
        }
    }

    suspend fun updateLastSkipUpdateVersionCode(versionCode: Int) {
        dataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setLastSkipUpdateVersionCode(versionCode)
                .build()
        }
    }

    suspend fun asyncHomeLayoutTypesetList(): List<AppSettings.HomeLayoutItem> {
        val defaultList = createDefaultHomeLayoutItems()
        val existingList = dataStore.data.first().homeLayoutTypesetList.toMutableList()

        return if (existingList.isEmpty()) {
            dataStore.updateData { currentSettings ->
                currentSettings.toBuilder()
                    .clearHomeLayoutTypeset()
                    .addAllHomeLayoutTypeset(defaultList)
                    .build()
            }
            defaultList
        } else {
            val existingTypes = existingList.map { it.type }.toSet()
            val missingItems = defaultList.filterNot { it.type in existingTypes }
            existingList.addAll(missingItems)
            existingList
        }
    }

    private fun createDefaultHomeLayoutItems(): List<AppSettings.HomeLayoutItem> {
        val defaultTypes = listOf(
            AppSettings.HomeLayoutType.Banner,
            AppSettings.HomeLayoutType.Announcement,
            AppSettings.HomeLayoutType.UpdateInfo,
            AppSettings.HomeLayoutType.Tools,
            AppSettings.HomeLayoutType.DownloadList
        )

        return defaultTypes.map { type ->
            AppSettings.HomeLayoutItem.newBuilder()
                .setType(type)
                .setIsHidden(false)
                .build()
        }
    }

    suspend fun updateHomeLayoutTypesetList(newList: List<AppSettings.HomeLayoutItem>) {
        dataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .clearHomeLayoutTypeset()
                .addAllHomeLayoutTypeset(newList)
                .build()
        }
    }


    suspend fun updateLastBulletinContent(content: String) {
        dataStore.updateData{ currentSettings ->
            currentSettings.toBuilder()
                .setLastBulletinContent(content)
                .build()
        }
    }

    suspend fun saveDownloadSAFUriString(uriString: String) {
        dataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setDownloadUri(uriString)
                .build()
        }
    }

}


fun AppSettings.HomeLayoutType.getDescription(): String = when (this) {
    AppSettings.HomeLayoutType.Banner -> "轮播图"
    AppSettings.HomeLayoutType.Announcement -> "公告信息"
    AppSettings.HomeLayoutType.UpdateInfo -> "更新信息"
    AppSettings.HomeLayoutType.Tools -> "工具列表"
    AppSettings.HomeLayoutType.DownloadList -> "下载列表"
    else -> this.name
}