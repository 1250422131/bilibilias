package com.imcys.bilibilias.weight

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.weight.ASIconButton

typealias OnUpdateEpisodeListMode = (AppSettings.EpisodeListMode) -> Unit

@Composable
fun ASEpisodeTitle(
    title: String,
    episodeListMode: AppSettings.EpisodeListMode,
    onUpdateEpisodeListMode: OnUpdateEpisodeListMode
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        Spacer(Modifier.weight(1f))
        ASIconButton(
            shape = CircleShape,
            onClick = {
                onUpdateEpisodeListMode.invoke(
                    if (episodeListMode == AppSettings.EpisodeListMode.EpisodeListMode_List) {
                        AppSettings.EpisodeListMode.EpisodeListMode_Grid
                    } else {
                        AppSettings.EpisodeListMode.EpisodeListMode_List
                    }
                )
            }
        ) {
            when (episodeListMode) {
                AppSettings.EpisodeListMode.UNRECOGNIZED,
                AppSettings.EpisodeListMode.EpisodeListMode_Grid -> {
                    Icon(
                        Icons.Outlined.Apps,
                        contentDescription = "表格显示",
                    )
                }

                AppSettings.EpisodeListMode.EpisodeListMode_List -> {
                    Icon(
                        Icons.AutoMirrored.Outlined.List,
                        contentDescription = "列表显示",
                    )
                }
            }
        }
    }
}