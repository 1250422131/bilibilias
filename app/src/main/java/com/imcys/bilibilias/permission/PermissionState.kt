package com.imcys.bilibilias.permission

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

@ExperimentalPermissionsApi
@Composable
internal fun rememberPermissionState(
    permission: String
): PermissionState {
    val context = LocalContext.current
    val permissionState = remember(permission) {
        PermissionStateImpl(permission, context)
    }
    PermissionLifecycleChecker(permissionState)
    return permissionState
}

@ExperimentalPermissionsApi
@Stable
internal class PermissionStateImpl(
    override val permission: String,
    private val context: Context
) : PermissionState {

    override var status: PermissionStatus by mutableStateOf(getPermissionStatus())

    override fun launchPermissionRequest() {
        val strings = permission.split(",")
        XXPermissions.with(context)
            .permission(strings)
            .request(
                object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (!allGranted) {
                            // toast("获取部分权限成功，但部分权限未正常授予")
                            return
                        }
                        // toast("获取录音和日历权限成功")
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        if (doNotAskAgain) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions)
                        } else {
                            // toast("获取录音和日历权限失败")
                        }
                    }
                }
            )
    }

    internal fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    private fun getPermissionStatus(): PermissionStatus {
        val strings = permission.split(",").toList()
        val hasPermission = XXPermissions.isGranted(context, strings)
        return if (hasPermission) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied(true)
        }
    }
}

@ExperimentalPermissionsApi
@Composable
internal fun PermissionLifecycleChecker(
    permissionState: PermissionStateImpl,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME
) {
    // activity每次走到onResume时检查权限状态
    LifecycleEventEffect(event = lifecycleEvent) {
        // 当没有授权时,再检查多一次,避免遗留
        if (permissionState.status != PermissionStatus.Granted) {
            permissionState.refreshPermissionStatus()
        }
    }
}
