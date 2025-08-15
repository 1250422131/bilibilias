package com.imcys.bilibilias.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.logic.search.SelfInfoUiState

@Composable
fun SelfAvatar(
    state: SelfInfoUiState,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onLogoutConfirmed: () -> Unit,
    onAvatarClick: (userInfo: SelfInfo) -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (state) {
            SelfInfoUiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            SelfInfoUiState.Guest -> {
                OutlinedButton(onLoginClick) {
                    Icon(Icons.AutoMirrored.Rounded.Login, "Login Icon")
                    Text("登录", Modifier.padding(start = 8.dp))
                }
            }

            is SelfInfoUiState.Success -> {
                val selfInfo = state.selfInfo
                SelfAvatarMenus(
                    selfInfo = selfInfo,
                    onLogoutConfirmed = onLogoutConfirmed,
                    onAvatarClick = onAvatarClick
                )
            }
        }
    }
}

@Composable
private fun SelfAvatarMenus(
    selfInfo: SelfInfo,
    onLogoutConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    onAvatarClick: (userInfo: SelfInfo) -> Unit = {},
) {
    var isDropdownMenuVisible by remember { mutableStateOf(false) }
    var isLogoutDialogVisible by remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = selfInfo.avatarUrl,
            contentDescription = "User Avatar",
            modifier = Modifier
                .padding(end = 8.dp)
                .size(36.dp)
                .clip(CircleShape)
                .clickable {
                    onAvatarClick(selfInfo)
                    isDropdownMenuVisible = true
                }
        )
    }
    DropdownMenu(
        expanded = isDropdownMenuVisible,
        onDismissRequest = { isDropdownMenuVisible = false },
        offset = DpOffset((-8).dp, 8.dp)
    ) {
        DropdownMenuItem(
            text = { Text("退出登录") },
            onClick = { isLogoutDialogVisible = true },
            leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Logout, "Logout Icon") },
        )
    }
    if (isLogoutDialogVisible) {
        LogoutConfirmationDialog(
            onDismissRequest = { isLogoutDialogVisible = false },
            onConfirmLogout = {
                onLogoutConfirmed()
                isLogoutDialogVisible = false
            }
        )
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmLogout) {
                Text("退出登录", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("取消")
            }
        },
        text = {
            Text("确定要退出登录吗？")
        }
    )
}