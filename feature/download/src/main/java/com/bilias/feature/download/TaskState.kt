package com.bilias.feature.download

import com.imcys.model.download.CacheFile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TaskState(val taskList: ImmutableList<CacheFile> = persistentListOf())
