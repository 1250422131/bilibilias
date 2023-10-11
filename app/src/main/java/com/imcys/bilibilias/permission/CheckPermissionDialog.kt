package com.imcys.bilibilias.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.getActivity
import com.imcys.bilibilias.base.utils.gotoApplicationSettings
import com.imcys.bilibilias.base.utils.hasPickMediaPermission
import com.imcys.bilibilias.base.utils.shouldShowRationale
import com.imcys.bilibilias.common.base.components.BottomSheetDialog
import com.imcys.bilibilias.common.base.config.CookieRepository
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissionDialog(onNavigateToAuthMethod: () -> Unit, onNavigateToHome: () -> Unit) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE) { granted ->
        if (!granted) {
            context.getActivity().apply {
                when {
                    shouldShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                        showDialog = true
                    }

                    else -> {
                        context.gotoApplicationSettings()
                    }
                }
            }
        }
    }
    LaunchedEffect(permissionState.status.isGranted) {
        if (context.hasPickMediaPermission()) {
            if (CookieRepository.isExpired) {
                onNavigateToAuthMethod()
            } else {
                onNavigateToHome()
            }
        } else {
            showDialog = true
        }
    }

    BottomSheetDialog(
        visible = showDialog,
        onDismissRequest = {},
        cancelable = false,
        canceledOnTouchOutside = false
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.app_permission_application_title),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                Modifier.padding(horizontal = 25.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.app_permission_application_msg),
                    fontSize = 18.sp
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        permissionState.launchPermissionRequest()
                    }
                },
                Modifier
                    .height(60.dp)
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(stringResource(R.string.app_permission_application_confirm))
            }
            Button(
                onClick = {
                    context.gotoApplicationSettings()
                    context.getActivity().finish()
                },
                Modifier
                    .height(60.dp)
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(),
            ) {
                Text(stringResource(R.string.app_permission_application_cancel))
            }
            Spacer(modifier = Modifier.height(256.dp))
        }
    }
}
