package com.bilias.feature.download

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TaskState(val taskList: ImmutableList<Task> = persistentListOf())
