package com.imcys.bilibilias.feature.settings.component

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.arkivanov.decompose.ComponentContext
import com.hjq.toast.Toaster
import com.imcys.bilibilias.core.common.download.DefaultConfig.DEFAULT_COMMAND
import com.imcys.bilibilias.core.common.download.DefaultConfig.DEFAULT_NAMING_RULE
import com.imcys.bilibilias.core.common.download.DefaultConfig.DEFAULT_STORE_PATH
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.model.data.UserData
import com.imcys.bilibilias.feature.common.BaseViewModel
import com.imcys.bilibilias.feature.settings.UserEditEvent
import com.imcys.bilibilias.feature.settings.UserEditableSettings
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.DevUtils
import dev.utils.BuildConfig
import dev.utils.app.ActivityUtils
import dev.utils.app.AppUtils
import dev.utils.app.AppUtils.getPackageName
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import java.io.File


class DefaultSettingsComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val asPreferencesDataSource: AsPreferencesDataSource,
    private val loginInfoDataSource: LoginInfoDataSource,
) : SettingsComponent, BaseViewModel<UserEditEvent, UserEditableSettings>(componentContext) {

    @Composable
    override fun models(events: Flow<UserEditEvent>): UserEditableSettings {
        val userData by asPreferencesDataSource.userData.collectAsState(
            initial = UserData(
                storagePath = DEFAULT_STORE_PATH,
                namingRule = DEFAULT_NAMING_RULE,
                autoMerge = true,
                autoImport = false,
                shouldAppcenter = true,
                command = DEFAULT_COMMAND

            ),
            context = viewModelScope.coroutineContext
        )
        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is UserEditEvent.onChangeAutoImport ->
                        asPreferencesDataSource.setAutoImportToBilibili(event.state)

                    is UserEditEvent.onChangeAutoMerge ->
                        asPreferencesDataSource.setAutoMerge(event.state)

                    is UserEditEvent.onEditCommand ->
                        asPreferencesDataSource.setCommand(event.text)

                    is UserEditEvent.onEditNamingRule ->
                        asPreferencesDataSource.setFileNameRule(event.rule)

                    is UserEditEvent.onSelectedStoragePath ->
                        asPreferencesDataSource.setFileStoragePath(event.path)

                    is UserEditEvent.onChangeWill ->
                        asPreferencesDataSource.setShouldAppcenter(event.state)

                    UserEditEvent.onLogout -> loginInfoDataSource.setLoginState(false)

                    UserEditEvent.ShareLog.NewLog -> {
                        val logFile = File(DevUtils.getContext().externalCacheDir, "log.txt")
                        shareLog(logFile)
                    }

                    UserEditEvent.ShareLog.OldLog -> {
                        val oldLogFile = File(DevUtils.getContext().externalCacheDir, "old_log.txt")
                        shareLog(oldLogFile)
                    }
                }
            }
        }
        return UserEditableSettings(
            storagePath = userData.storagePath ?: DEFAULT_STORE_PATH,
            namingRule = userData.namingRule ?: DEFAULT_NAMING_RULE,
            autoMerge = true,
            autoImport = false,
            command = userData.command ?: DEFAULT_COMMAND,
            shouldAppcenter = true
        )
    }

    @AssistedFactory
    interface Factory : SettingsComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultSettingsComponent
    }
}

private fun shareLog(logFile: File) {
    if (logFile.exists()) {
        val context = DevUtils.getContext()
        val uri = FileProvider.getUriForFile(
            context,
            "${AppUtils.getPackageName()}.fileProvider",
            logFile
        )
        val title = "bilibilias"
        val shareIntent = ShareCompat.IntentBuilder(context)
            .setStream(uri)
            .setChooserTitle(title)
            .setSubject(title)
            .setType("text/plain")
            .intent
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .apply {
                clipData = ClipData.newRawUri(title, uri)
            }
        val chooserIntent = Intent.createChooser(shareIntent, title)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AppUtils.startActivity(chooserIntent)
    } else {
        Toaster.show("日志文件不存在")
        Napier.d { "日志文件不存在" }
    }
}