package com.imcys.bilibilias.feature.settings.component

import android.content.ClipData
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.datastore.AsCookieStoreDataSource
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.UsersDataSource
import com.imcys.bilibilias.core.download.DefaultConfig.DEFAULT_COMMAND
import com.imcys.bilibilias.core.download.DefaultConfig.DEFAULT_NAMING_RULE
import com.imcys.bilibilias.core.download.DefaultConfig.DEFAULT_STORE_PATH
import com.imcys.bilibilias.core.model.data.UserData
import com.imcys.bilibilias.feature.common.BaseViewModel
import com.imcys.bilibilias.feature.settings.UserEditEvent
import com.imcys.bilibilias.feature.settings.UserEditableSettings
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.DevUtils
import dev.utils.app.AppUtils
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import java.io.File

class DefaultSettingsComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val asPreferencesDataSource: AsPreferencesDataSource,
    private val asCookieStoreDataSource: AsCookieStoreDataSource,
    private val usersDataSource: UsersDataSource,
) : BaseViewModel<UserEditEvent, UserEditableSettings>(componentContext),
    SettingsComponent {

    @Composable
    override fun models(events: Flow<UserEditEvent>): UserEditableSettings {
        val userData by asPreferencesDataSource.userData.collectAsState(
            initial = UserData(
                storageFolder = DEFAULT_STORE_PATH,
                fileNamesConvention = DEFAULT_NAMING_RULE,
                ffmpegCommand = DEFAULT_COMMAND,
            ),
            context = viewModelScope.coroutineContext,
        )
        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is UserEditEvent.EditCommand ->
                        asPreferencesDataSource.setFfmpegCommand(event.text)

                    is UserEditEvent.EditNamingRule ->
                        asPreferencesDataSource.setFileNamesConvention(event.rule)

                    is UserEditEvent.SelectedStoragePath ->
                        asPreferencesDataSource.setStorageFolder(event.path)

                    UserEditEvent.Logout -> usersDataSource.setLoginState(false)

                    UserEditEvent.ShareLog.NewLog -> {
                        val logFile = File(DevUtils.getContext().externalCacheDir, "log.txt")
                        shareLog(logFile)
                    }

                    UserEditEvent.ShareLog.OldLog -> {
                        val oldLogFile = File(DevUtils.getContext().externalCacheDir, "old_log.txt")
                        shareLog(oldLogFile)
                    }

                    else -> Unit
                }
            }
        }
        return UserEditableSettings(
            storagePath = userData.storageFolder ?: DEFAULT_STORE_PATH,
            namingRule = userData.fileNamesConvention ?: DEFAULT_NAMING_RULE,
            command = userData.ffmpegCommand ?: DEFAULT_COMMAND,
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
            logFile,
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
        Napier.d { "日志文件不存在" }
    }
}
