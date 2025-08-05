package com.imcys.bilibilias.ui.play.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.ui.play.PlayScreen
import kotlinx.serialization.Serializable

@Serializable
data class PlayRoute(
    val title: String,
    val cover: String?,
    val savePath: String,
) {
    companion object {

        fun fromDownloadSegment(downloadSegment: DownloadSegment): PlayRoute {
            return PlayRoute(
                title = downloadSegment.title,
                cover = downloadSegment.cover,
                savePath = downloadSegment.savePath,
            )
        }
    }
}

fun NavGraphBuilder.playScreen(
    onToBack: () -> Unit,
) {
    composable<PlayRoute> {
        PlayScreen(onToBack, it.toRoute<PlayRoute>())
    }
}
fun NavController.navigateToPlay(
    playRoute: PlayRoute,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    navigate(playRoute, builder ?: {})
}