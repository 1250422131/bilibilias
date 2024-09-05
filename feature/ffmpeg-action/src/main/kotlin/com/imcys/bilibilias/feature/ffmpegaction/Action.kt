package com.imcys.bilibilias.feature.ffmpegaction

sealed interface Action {
    data object ExecuteCommand : Action
    data class CreateNewFile(val uri: String) : Action
    data class UpdateCommand(val command: String) : Action
    data class AddResource(val res: ResourceFile) : Action
    data object Increase : Action
    data object Decrease : Action
}

data class State(
    val command: String,
    val videoName: String,
    val audioName: String,
    val count: Int,
    val resources: List<ResourceFile>,
    val action: (Action) -> Unit,
)
