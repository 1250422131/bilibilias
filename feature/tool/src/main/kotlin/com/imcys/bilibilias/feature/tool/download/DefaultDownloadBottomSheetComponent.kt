package com.imcys.bilibilias.feature.tool.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.Sources
import com.imcys.bilibilias.core.model.video.ViewIds
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.imcys.bilibilias.feature.common.PresenterModel
import com.imcys.bilibilias.feature.tool.download.DownloadBottomSheetComponent.Model
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultDownloadBottomSheetComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("title") private val title: String,
    @Assisted("author") private val author: String,
    @Assisted private val id: ViewIds,
    private val videoRepository: VideoRepository,
) : PresenterModel<Model, DownloadTypeBottomSheetEvent>(),
    DownloadBottomSheetComponent,
    ComponentContext by componentContext {

    @Composable
    override fun models(events: Flow<DownloadTypeBottomSheetEvent>): Model {
        var title by remember { mutableStateOf(title) }
        var author by remember { mutableStateOf(author) }
        var formats = remember { mutableStateListOf<Format>() }
        LaunchedEffect(Unit) {
            val playUrl = videoRepository.playerPlayUrl(id.aid, id.bvid, id.cid)
            val duration = playUrl.dash.duration
            (playUrl.dash.video.mapToFormat(duration) + playUrl.dash.audio.mapToFormat(duration)).also(
                formats::addAll,
            )
        }
        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is DownloadTypeBottomSheetEvent.ChangesAuthor -> author = event.text
                    is DownloadTypeBottomSheetEvent.ChangesTitle -> title = event.text
                }
            }
        }
        return Model(
            title = title,
            pagePart = "TODO()",
            author = author,
            format = formats,
        )
    }

    @AssistedFactory
    interface Factory : DownloadBottomSheetComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("title") title: String,
            @Assisted("author") author: String,
            id: ViewIds,
        ): DefaultDownloadBottomSheetComponent
    }
}

private fun List<Sources>.mapToFormat(duration: Int) = map {
    Format(
        formatId = it.id,
        url = it.baseUrl,
        codecId = it.codecid,
        codecs = it.codecs,
        mimeType = it.mimeType,
        bandwidth = it.bandwidth,
        duration = duration,
        width = it.width,
        height = it.height,
        sar = it.sar,
        fps = it.frameRate,
    )
}
