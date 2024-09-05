package com.imcys.bilibilias.feature.ffmpegaction

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.ffmpeg.IFFmpegWork
import com.imcys.bilibilias.feature.common.BaseViewModel2
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlin.math.max

internal class DefaultFfmpegActionComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val work: IFFmpegWork,
) : BaseViewModel2<State, Unit>(),
    FfmpegActionComponent,
    ComponentContext by componentContext {

    @Composable
    override fun models(events: Flow<Unit>): State {
        var videoName by remember { mutableStateOf("videoName") }
        var audioName by remember { mutableStateOf("audioName") }

        var videoUri by remember { mutableStateOf<Uri?>(null) }
        var audioUri by remember { mutableStateOf<Uri?>(null) }

        var newFile by remember { mutableStateOf<String?>(null) }

        var command by remember { mutableStateOf("-y -i {input} -i {input} -c copy {output}") }

        var count by remember { mutableIntStateOf(0) }
        val resources = remember { mutableStateListOf<ResourceFile>() }
        return State(
            command = command,
            videoName = videoName,
            audioName = audioName,
            count = count,
            resources = resources,
        ) {
            when (it) {
                Action.ExecuteCommand -> {
                    work.execute(
                        template = command,
                        outputUri = newFile.toString(),
                        contentSourcesUri = resources.map { r -> r.uri.toString() }.toTypedArray(),
                        onSuccess = {},
                        onFailure = {},
                    )
                }

                is Action.UpdateCommand -> command = it.command
                is Action.CreateNewFile -> newFile = it.uri
                is Action.AddResource -> resources.add(it.res)
                is Action.Increase -> count++
                is Action.Decrease -> {
                    count = max(--count, 0)
                    resources.removeLastOrNull()
                }
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
