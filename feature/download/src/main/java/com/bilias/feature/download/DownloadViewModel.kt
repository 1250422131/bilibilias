package com.bilias.feature.download

import android.app.*
import android.content.*
import androidx.lifecycle.*
import com.imcys.network.download.*
import dagger.hilt.android.lifecycle.*
import io.github.aakira.napier.*
import kotlinx.collections.immutable.*
import kotlinx.coroutines.flow.*
import java.io.*
import javax.inject.*

// 国内包名
const val PKGNAME_BILIBILI_INTERNAL = "tv.danmaku.bili"

// 国际包名
const val PKGNAME_BILIBILI_ABROAD = "com.bilibili.app.in"

// 平板包名
const val PKGNAME_BILIBILI_IPAD = "tv.danmaku.bilibilihd"

// 概念包名
const val PKGNAME_BILIBILI_CONCEPT = "com.bilibili.app.blue"

// 国内默认缓存下载路径
val TYPE_CACHE_FILE_PATH_INTERNAL = PKGNAME_BILIBILI_INTERNAL + File.separator + "download"

// 国际默认缓存下载路径
val TYPE_CACHE_FILE_PATH_ABROAD =
    File.separator + PKGNAME_BILIBILI_ABROAD + File.separator + "download"

// 平板默认缓存下载路径
val TYPE_CACHE_FILE_PATH_IPAD = File.separator + PKGNAME_BILIBILI_IPAD + File.separator + "download"

// 概念默认缓存下载路径
val TYPE_CACHE_FILE_PATH_CONCEPT =
    File.separator + PKGNAME_BILIBILI_CONCEPT + File.separator + "download"

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadManager: DownloadManage,
    application: Application
) : AndroidViewModel(application) {
    private val _taskState = MutableStateFlow(TaskState())
    val taskState = _taskState.asStateFlow()

    init {
        downloadManager.setProgressListener { map ->
            _taskState.update {
                it.copy(progress = map.toImmutableMap())
            }
        }
        taskList(getApplication())
    }

    fun taskList(context: Context) {
        val allTask = downloadManager.getAllTask(context.downloadDir.absolutePath)
            .toImmutableList()
        Napier.d { "task$allTask" }
        _taskState.update { it.copy(taskList = allTask) }
    }

}
