package com.imcys.bilibilias.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.logic.search.SelfInfoUiState

@Composable
fun SelfAvatar(
    state: SelfInfoUiState,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onAvatarClick: (userInfo: SelfInfo) -> Unit,
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
                AsyncImage(
                    model = selfInfo.avatarUrl,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable {
                            onAvatarClick(selfInfo)
                        }
                )
            }
        }
    }
}