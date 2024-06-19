package com.imcys.bilibilias.core.database.util

import android.net.Uri
import com.imcys.bilibilias.core.database.model.Task
import com.imcys.bilibilias.core.database.model.TaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import kotlinx.datetime.Instant

internal object TypeConverters {
    fun getRequiredConverters(): List<Class<*>> {
        return emptyList()
    }

    fun fileTypeToString(value: FileType): String {
        return when (value) {
            FileType.VIDEO -> "VIDEO"
            FileType.AUDIO -> "AUDIO"
            else -> throw IllegalArgumentException("Can't convert enum to string, unknown enum value: $value")
        }
    }

    fun stateToString(value: State): String {
        return when (value) {
            State.START -> "START"
            State.PENDING -> "PENDING"
            State.RUNNING -> "RUNNING"
            State.COMPLETED -> "COMPLETED"
            State.ERROR -> "ERROR"
            else -> throw IllegalArgumentException("Can't convert enum to string, unknown enum value: $value")
        }
    }

    fun stringToFileType(value: String): FileType {
        return when (value) {
            "VIDEO" -> FileType.VIDEO
            "AUDIO" -> FileType.AUDIO
            else -> throw IllegalArgumentException("Can't convert value to enum, unknown value: $value")
        }
    }

    fun stringToState(value: String): State {
        return when (value) {
            "START" -> State.START
            "PENDING" -> State.PENDING
            "RUNNING" -> State.RUNNING
            "COMPLETED" -> State.COMPLETED
            "ERROR" -> State.ERROR
            else -> throw IllegalArgumentException("Can't convert value to enum, unknown value: $value")
        }
    }
}

internal fun Task.maptToTaskEntity(): TaskEntity {
    return TaskEntity(
        uri = uri.toString(),
        created = created.toEpochMilliseconds(),
        aid = aid,
        bvid = bvid,
        cid = cid,
        fileType = TypeConverters.fileTypeToString(fileType),
        subTitle = subTitle,
        title = title,
        state = TypeConverters.stateToString(state),
        bytesSentTotal = bytesSentTotal,
        contentLength = contentLength
    )
}

internal fun TaskEntity.mapToTask(): Task {
    return Task(
        uri = Uri.parse(uri),
        created = Instant.fromEpochMilliseconds(created),
        aid = aid,
        bvid = bvid,
        cid = cid,
        fileType = TypeConverters.stringToFileType(fileType),
        subTitle = subTitle,
        title = title,
        state = TypeConverters.stringToState(state),
        bytesSentTotal = bytesSentTotal,
        contentLength = contentLength
    )
}