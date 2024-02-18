package com.bilias.feature.download

import com.imcys.model.download.*
import kotlinx.collections.immutable.*

data class TaskState(
    val taskList: ImmutableList<CacheFile> = persistentListOf(),
    val progress: ImmutableMap<Task, Int> = persistentMapOf()
)
