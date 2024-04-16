package com.imcys.bilibilias.core.ui.radio

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.imcys.bilibilias.core.designsystem.component.AsRadioButton

@Composable
fun FileTypeRadioGroup(typeState: FileTypeState) {
    Row {
        AsRadioButton(
            selected = typeState.current == TaskType.ALL,
            onClick = { typeState.current = TaskType.ALL },
            text = { Text(text = "ALL") },
        )
        AsRadioButton(
            selected = typeState.current == TaskType.VIDEO,
            onClick = { typeState.current = TaskType.VIDEO },
            text = { Text(text = "VIDEO") },
        )
        AsRadioButton(
            selected = typeState.current == TaskType.AUDIO,
            onClick = { typeState.current = TaskType.AUDIO },
            text = { Text(text = "AUDIO") },
        )
    }
}

@Composable
fun rememberFileTypeState(
    initialState: TaskType = TaskType.ALL
): FileTypeState {
    return remember {
        FileTypeState(initialState)
    }
}

@Stable
class FileTypeState(initialState: TaskType) {
    var current by mutableStateOf(initialState)
        internal set
}

enum class TaskType {
    ALL,
    VIDEO,
    AUDIO,
}
