package com.imcys.bilibilias.common.update

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.clientVersionStalenessDays
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine


@OptIn(DelicateCoroutinesApi::class)
class GooglePlayAppUpdateManage(
    context: Context,
    private val appSettingsRepository: AppSettingsRepository
) : ASAppUpdateManage() {
    private lateinit var appUpdateManager: AppUpdateManager

    private var lastAppUpdateType = AppUpdateType.FLEXIBLE


    private val appSettingsFlow = appSettingsRepository.appSettingsFlow

    private var lastSkipUpdateVersionCode = 0

    init {
        GlobalScope.launch {
            appSettingsFlow.collect {
                lastSkipUpdateVersionCode = it.lastSkipUpdateVersionCode
            }
        }
    }

    override suspend fun checkAppImmediateUpdate(): Boolean {
        return checkUpdateAvailability(AppUpdateType.IMMEDIATE)
    }

    override suspend fun checkAppFlexibleUpdate(): Boolean {
        return checkUpdateAvailability(AppUpdateType.FLEXIBLE)
    }

    suspend fun getUpdateVersion(): Int {
        return suspendCancellableCoroutine { cont ->
            appUpdateManager
                .appUpdateInfo
                .addOnSuccessListener { appUpdateInfo ->
                    cont.resumeWith(Result.success(appUpdateInfo.availableVersionCode()))
                }
        }
    }

    fun startUpdate(
        activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
        updateFinish: () -> Unit
    ) {
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // 如果已经下载完成，直接提示完成安装
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    updateFinish()
                    return@addOnSuccessListener
                }

                // 如果有更新，启动更新
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(lastAppUpdateType).build()
                    )
                }
            }
    }


    fun registerFlexibleUpdateListener(listener: InstallStateUpdatedListener) {
        // 注册灵活更新监听器
        appUpdateManager.registerListener(listener)
    }

    fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }


    private suspend fun checkUpdateAvailability(updateType: Int): Boolean {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        return suspendCancellableCoroutine { cont ->
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

                // 如果用户选择了跳过该版本更新，则不再提示
                if (lastSkipUpdateVersionCode == appUpdateInfo.availableVersionCode()) {
                    cont.resumeWith(Result.success(false))
                    return@addOnSuccessListener
                }

                // 灵活版本3天内不提示更新
                if (updateType == AppUpdateType.FLEXIBLE && (appUpdateInfo.clientVersionStalenessDays
                        ?: -1) < 3
                ) {
                    cont.resumeWith(Result.success(false))
                    return@addOnSuccessListener
                }

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(updateType)
                ) {
                    lastAppUpdateType = updateType
                    cont.resumeWith(Result.success(true))
                } else {
                    cont.resumeWith(Result.success(false))
                }
            }
        }
    }

}