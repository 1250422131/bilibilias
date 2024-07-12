package com.imcys.bilibilias.core.database.dao

import android.content.Context
import androidx.core.net.toUri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.imcys.bilibilias.core.database.AsDatabase
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DownloadTaskDaoTest {
    private lateinit var downloadTaskDao: DownloadTaskDao
    private lateinit var db: AsDatabase

    @BeforeTest
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AsDatabase::class.java,
        ).build()
        downloadTaskDao = db.downloadTaskDao()
    }

    @AfterTest
    fun closeDb() = db.close()

    @Test
    fun kkk() = runTest {

    }

    @Test
    fun downloadTaskDao_delete_items_by_ids() = runTest {
        val downloadTaskEntities = (0..5).map { testDownloadTaskEntity(it) }
        downloadTaskEntities.forEach {
            downloadTaskDao.insertTask(it)
        }
        val (toDelete, toKeep) = downloadTaskEntities.partition { it.id.toInt() % 2 == 0 }
        downloadTaskDao.delete(toDelete.map(DownloadTaskEntity::id))

        assertEquals(
            toKeep.map(DownloadTaskEntity::id)
                .toSet(),
            downloadTaskDao.findAllTask().first()
                .map { it.id }
                .toSet(),
        )
    }
}

private fun testDownloadTaskEntity(
    id: Int,
    title: String = "",
) = DownloadTaskEntity(
    uri = "".toUri(),
    aid = 0,
    bvid = "",
    cid = 0,
    fileType = FileType.VIDEO,
    subTitle = "",
    title = title,
    created = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()),
    state = State.ERROR,
    id = id,
)
