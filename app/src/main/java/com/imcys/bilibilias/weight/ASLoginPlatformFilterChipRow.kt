package com.imcys.bilibilias.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.WebAsset
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.database.entity.LoginPlatform

@Composable
fun ASLoginPlatformFilterChipRow(loginPlatforms: List<LoginPlatform>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        loginPlatforms.forEach {
            AssistChip(
                onClick = {},
                label = {
                    Text(it.name)
                },
                leadingIcon = {
                    Icon(
                        imageVector = when(it){
                            LoginPlatform.WEB -> Icons.Outlined.WebAsset
                            LoginPlatform.MOBILE -> Icons.Outlined.PhoneIphone
                            LoginPlatform.TV -> Icons.Outlined.Tv
                        },
                        contentDescription = it.name,
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                },
            )
        }
    }
}