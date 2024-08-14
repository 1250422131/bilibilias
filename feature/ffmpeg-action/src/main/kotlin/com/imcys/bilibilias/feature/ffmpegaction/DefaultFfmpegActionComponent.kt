package com.imcys.bilibilias.feature.ffmpegaction

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.BaseViewModel2
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

internal class DefaultFfmpegActionComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : BaseViewModel2<State, Unit>(),
    FfmpegActionComponent,
    ComponentContext by componentContext {

    @Composable
    override fun models(events: Flow<Unit>): State {
        var videoName by remember { mutableStateOf("") }
        var audioName by remember { mutableStateOf("") }

        var videoUri by remember { mutableStateOf<Uri?>(null) }
        var audioUri by remember { mutableStateOf<Uri?>(null) }

        var newFile by remember { mutableStateOf<String?>(null) }
        return State(
            videoName = videoName,
            audioName = audioName,
        ) {
            when (it) {
                is Action.UpdateAudioResource -> {
                    videoName = it.res.name
                    videoUri = it.res.uri
                }

                is Action.UpdateVideoResource -> {
                    audioName = it.res.name
                    audioUri = it.res.uri
                }

                Action.ExecuteCommand -> {
                }
                is Action.CreateNewFile -> newFile = it.uri
            }
        }
    }

    @AssistedFactory
    interface Factory : FfmpegActionComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultFfmpegActionComponent
    }
}
