package com.imcys.bilibilias.core.database.dao

import android.net.Uri
import com.ctrip.sqllin.dsl.Database
import com.ctrip.sqllin.dsl.sql.X
import com.ctrip.sqllin.dsl.sql.clause.AND
import com.ctrip.sqllin.dsl.sql.clause.EQ
import com.ctrip.sqllin.dsl.sql.clause.SET
import com.ctrip.sqllin.dsl.sql.clause.WHERE
import com.ctrip.sqllin.dsl.sql.statement.SelectStatement
import com.imcys.bilibilias.core.database.model.Task
import com.imcys.bilibilias.core.database.model.TaskEntity
import com.imcys.bilibilias.core.database.model.TaskEntityTable
import com.imcys.bilibilias.core.database.util.TypeConverters
import com.imcys.bilibilias.core.database.util.mapToTask
import com.imcys.bilibilias.core.database.util.maptToTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DownloadTaskDao2 @Inject constructor(private val database: Database) {

    private lateinit var selectStatement: SelectStatement<TaskEntity>
    private val channel = Channel<Unit>(Channel.CONFLATED)
    fun findAllFlow(): Flow<List<Task>> = flow {
        channel.trySend(Unit)
        for (item in channel) {
            emit(findAll())
        }
    }

    suspend fun findAll(): List<Task> {
        database suspendedScope {
            TaskEntityTable { table ->
                selectStatement = table SELECT X
            }
        }
        return selectStatement.getResults().map(TaskEntity::mapToTask)
    }

    suspend fun insertOrUpdateTask(t: Task) {
        val tasks = findByIdWithFileType(t)
        if (tasks.isEmpty()) {
            insertTask(t)
        } else {
            val task = tasks.single()
            updateTask(
                task.copy(
                    uri = t.uri,
                    created = t.created,
                    subTitle = t.subTitle,
                    title = t.title,
                    state = t.state,
                    bytesSentTotal = t.bytesSentTotal,
                    contentLength = t.contentLength,
                )
            )
        }
    }

    suspend fun updateTask(t: Task) {
        database suspendedScope {
            TaskEntityTable { table ->
                table UPDATE SET {
                    uri = t.uri.toString()
                    created = t.created.toEpochMilliseconds()
                    title = t.title
                    subTitle = t.subTitle
                    state = TypeConverters.stateToString(t.state)
                    bytesSentTotal = t.bytesSentTotal
                    contentLength = t.contentLength
                } WHERE (
                        (aid EQ t.aid) AND
                                (bvid EQ t.bvid) AND
                                (cid EQ t.cid) AND
                                (fileType EQ TypeConverters.fileTypeToString(t.fileType))
                        )
            }
        }
        channel.trySend(Unit)
    }

    suspend fun findById(aid: Aid, bvid: Bvid, cid: Cid): List<Task> {
        database suspendedScope {
            TaskEntityTable { table ->
                selectStatement =
                    table SELECT WHERE((this.aid EQ aid) AND (this.bvid EQ bvid) AND (this.cid EQ cid))
            }
        }
        return selectStatement.getResults().map(TaskEntity::mapToTask)
    }

    suspend fun findByIdWithFileType(t: Task): List<Task> {
        database suspendedScope {
            TaskEntityTable { table ->
                selectStatement =
                    table SELECT
                            WHERE(
                                (aid EQ t.aid) AND
                                        (bvid EQ t.bvid) AND
                                        (cid EQ t.cid) AND
                                        (fileType EQ TypeConverters.fileTypeToString(t.fileType))
                            )
            }
        }
        return selectStatement.getResults().map(TaskEntity::mapToTask)
    }


    suspend fun insertTask(t: Task) {
        database suspendedScope {
            TaskEntityTable { table ->
                table INSERT t.maptToTaskEntity()
            }
        }
        channel.trySend(Unit)
    }

    suspend fun updateStateByUri(newState: State, u: Uri) {
        database {
            TaskEntityTable { table ->
                table UPDATE SET {
                    state = TypeConverters.stateToString(newState)
                } WHERE (uri EQ u.toString())
            }
        }
        channel.trySend(Unit)
    }

    suspend fun updateStateByIdWithFileType(
        newState: State,
        a: Aid,
        b: Bvid,
        c: Cid,
        type: FileType
    ) {
        database suspendedScope {
            TaskEntityTable { table ->
                table UPDATE SET {
                    state = TypeConverters.stateToString(newState)
                } WHERE ((aid EQ a) AND
                        (bvid EQ b) AND
                        (cid EQ c) AND
                        (fileType EQ TypeConverters.fileTypeToString(type)))
            }
        }
        channel.trySend(Unit)
    }

    suspend fun updateProgressWithStateByUri(
        newState: State,
        currentOffset: Long,
        totalLength: Long,
        u: Uri,
    ) {
        database {
            TaskEntityTable { table ->
                table UPDATE SET {
                    state = TypeConverters.stateToString(newState)
                    bytesSentTotal = currentOffset
                    contentLength = totalLength
                } WHERE (uri EQ u.toString())
            }
        }
        channel.trySend(Unit)
    }

    suspend fun updateProgressWithStateByIdWithFileType(
        newState: State,
        currentOffset: Long,
        totalLength: Long,
        a: Aid,
        b: Bvid,
        c: Cid,
        type: FileType
    ) {
        database suspendedScope {
            TaskEntityTable { table ->
                table UPDATE SET {
                    state = TypeConverters.stateToString(newState)
                    bytesSentTotal = currentOffset
                    contentLength = totalLength
                } WHERE ((aid EQ a) AND
                        (bvid EQ b) AND
                        (cid EQ c) AND
                        (fileType EQ TypeConverters.fileTypeToString(type)))
            }
        }
        channel.trySend(Unit)
    }
}