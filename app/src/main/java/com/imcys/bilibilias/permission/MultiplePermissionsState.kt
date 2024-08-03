package com.imcys.bilibilias.permission

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.imcys.common.utils.getActivity
import com.imcys.common.utils.shouldShowRationale

@ExperimentalPermissionsApi
@Composable
public fun rememberMultiplePermissionsState(
    permissions: List<String>
): MultiplePermissionsState {
    return rememberMutableMultiplePermissionsState(permissions)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun rememberMutableMultiplePermissionsState(permissions: List<String>): MultiplePermissionsState {
    val mutablePermissions = rememberMutablePermissionsState(permissions)

    val multiplePermissionsState = remember(permissions) {
        MultiplePermissionStateImpl(mutablePermissions)
    }
    return multiplePermissionsState
}

@ExperimentalPermissionsApi
@Composable
private fun rememberMutablePermissionsState(
    permissions: List<String>
): List<MutablePermissionState> {
    // Create list of MutablePermissionState for each permission
    val context = LocalContext.current
    val activity = context.getActivity()
    val mutablePermissions: List<MutablePermissionState> =
        remember(permissions) {
            return@remember permissions.map { MutablePermissionState(it, context, activity) }
        }
    for (permissionState in mutablePermissions) {
        key(permissionState.permission) {
            permissionState.launchPermissionRequest()
            permissionState.refreshPermissionStatus()
        }
    }

    return mutablePermissions
}

@ExperimentalPermissionsApi
@Stable
internal class MutablePermissionState(
    override val permission: String,
    private val context: Context,
    private val activity: Activity
) : PermissionState {

    override var status: PermissionStatus by mutableStateOf(getPermissionStatus())

    override fun launchPermissionRequest() {
        XXPermissions.with(context)
            .permission(permission)
            .request(
                object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    }

                    override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    }
                }
            )
    }

    internal fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    private fun getPermissionStatus(): PermissionStatus {
        val hasPermission = XXPermissions.isGranted(context, permission)
        return if (hasPermission) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied(activity.shouldShowRationale(permission))
        }
    }
}
@ExperimentalPermissionsApi
class MultiplePermissionStateImpl(
    override val permissions: List<PermissionState>
) : MultiplePermissionsState {
    override val allPermissionsGranted: Boolean by derivedStateOf {
        permissions.all { it.status.isGranted } || revokedPermissions.isEmpty()
    }
    override val revokedPermissions: List<PermissionState> by derivedStateOf {
        permissions.filter { it.status != PermissionStatus.Granted }
    }
    override val shouldShowRationale: Boolean by derivedStateOf {
        permissions.any { it.status.shouldShowRationale }
    }

    override fun launchMultiplePermissionRequest() {
        permissions.forEach {
            it.launchPermissionRequest()
        }
    }
}
