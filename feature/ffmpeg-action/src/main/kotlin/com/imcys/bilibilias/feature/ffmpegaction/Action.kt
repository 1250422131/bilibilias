package com.imcys.bilibilias.feature.ffmpegaction

sealed interface Action {
    data class UpdateVideoResource(val res: ResourceFile) : Action
    data class UpdateAudioResource(val res: ResourceFile) : Action
    data object ExecuteCommand : Action
    data class CreateNewFile(val uri: String) : Action
    data class UpdateCommand(val command: String) : Action
}

data class State(
    val command: String,
    val videoName: String,
    val audioName: String,
    val action: (Action) -> Unit,
)
