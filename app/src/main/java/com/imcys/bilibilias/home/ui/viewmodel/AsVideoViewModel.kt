package com.imcys.bilibilias.home.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.asResult
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.network.danmaku.DanmakuRepository
import com.imcys.bilibilias.core.datastore.LoginInfoDataSource
import com.imcys.bilibilias.core.domain.GetDmDataWriteFileUseCase
import com.imcys.bilibilias.core.domain.GetViewTripleUseCase
import com.imcys.bilibilias.core.model.space.FavouredFolder
import com.imcys.bilibilias.core.model.video.ViewTriple
import com.imcys.bilibilias.core.network.di.ApiIOException
import com.imcys.bilibilias.core.network.repository.UserRepository
import com.imcys.bilibilias.core.network.repository.UserSpaceRepository
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.imcys.bilibilias.danmaku.change.CCJsonToAss
import com.imcys.bilibilias.danmaku.change.DmXmlToAss
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.DashVideoPlayBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.imcys.bilibilias.home.ui.model.VideoCCInfo
import com.imcys.bilibilias.home.ui.model.VideoPageListData
import com.imcys.bilibilias.home.ui.model.toDashVideoPlayBean
import com.imcys.bilibilias.home.ui.viewmodel.player.ViewUiState
import com.microsoft.appcenter.analytics.Analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

sealed class Event {
    data class ShowToast(val text: String) : Event()
    data class ToolBarReportChange(val viewTriple: ViewTriple) : Event()
    data class ShowFavouredDialog(val favouredFolder: FavouredFolder) : Event()
}

@HiltViewModel
class AsVideoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val danmakuRepository: DanmakuRepository,
    private val danmakuRepository2: com.imcys.bilibilias.core.network.repository.DanmakuRepository,
    private val userSpaceRepository: UserSpaceRepository,
    private val videoRepository: VideoRepository,
    private val userRepository: UserRepository,
    private val loginInfoDataSource: LoginInfoDataSource,
    getViewTripleUseCase: GetViewTripleUseCase,
    getDmDataWriteFileUseCase: GetDmDataWriteFileUseCase
) : ViewModel() {
    val _effect = Channel<Event>(Channel.UNLIMITED)
    val bvid = savedStateHandle.getStateFlow("bvId", "")
    val asVideoUiState = bvid.flatMapLatest {
        viewDetailUiState(
            it,
            videoRepository,
            userRepository,
            getViewTripleUseCase,
            getDmDataWriteFileUseCase
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ViewUiState.Loading)

    @Inject
    lateinit var http: HttpClient

    @Inject
    lateinit var networkService: NetworkService

    /**
     * 缓存视频
     * @param videoBaseBean VideoBaseBean
     * @param videoPageListData VideoPageListData
     */
    fun downloadVideo(
        view: View,
        videoBaseBean: VideoBaseBean,
        videoPageListData: VideoPageListData,
    ) {
        val context = view.context
        val loadDialog = DialogUtils.loadDialog(context).apply { show() }

        viewModelScope.launchUI {
            if ((context as AsVideoActivity).userBaseBean.data.level >= 2) {
                // 并发
                val dashVideoPlayDeferred =
                    async { networkService.viewDash(context.bvid, context.cid, 64) }
                val dashBangumiPlayDeferred =
                    async { networkService.pgcPlayUrl(context.cid, 64) }

                // 等待两个请求的结果
                val dashVideoPlayBean = dashVideoPlayDeferred.await()
                val dashBangumiPlay = dashBangumiPlayDeferred.await()

                // 这里再检验一次，是否为404内容
                if (dashVideoPlayBean.code == 0) {
                    DialogUtils.downloadVideoDialog(
                        context,
                        videoBaseBean,
                        videoPageListData,
                        dashVideoPlayBean,
                        networkService
                    ).show()
                } else if (dashBangumiPlay.code == 0) {
                    DialogUtils.downloadVideoDialog(
                        context,
                        videoBaseBean,
                        videoPageListData,
                        dashBangumiPlay.toDashVideoPlayBean(),
                        networkService
                    ).show()
                }

                loadDialog.cancel()
            } else {
                AsDialog.init(context).build {
                    config = {
                        title = "止步于此"
                        content =
                            "鉴于你的账户未转正，请前往B站完成答题，否则无法为您提供缓存服务。\n" +
                                    "作者也是B站UP主，见到了许多盗取视频现象，更有甚者缓存番剧后发布内容到其他平台。\n" +
                                    "而你的账户甚至是没有转正的，bilibilias自然不会想提供服务。"
                        positiveButtonText = "知道了"
                        positiveButton = {
                            it.cancel()
                        }
                    }
                }.show()
            }
        }
    }

    /**
     * 缓存番剧
     * @param videoBaseBean VideoBaseBean
     * @param bangumiSeasonBean BangumiSeasonBean
     */
    fun downloadBangumiVideo(
        view: View,
        videoBaseBean: VideoBaseBean,
        bangumiSeasonBean: BangumiSeasonBean,
    ) {
        val context = view.context

        val loadDialog = DialogUtils.loadDialog(context).apply { show() }

        viewModelScope.launchUI {
            if ((context as AsVideoActivity).userBaseBean.data.level >= 2) {
                // 并发
                val dashVideoPlayDeferred =
                    async { networkService.viewDash(context.bvid, context.cid, 94) }
                val dashBangumiPlayDeferred =
                    async { networkService.pgcPlayUrl(context.cid, 64) }

                // 等待两个请求的结果
                val dashVideoPlayBean = dashVideoPlayDeferred.await()
                val dashBangumiPlay = dashBangumiPlayDeferred.await()

                // 这里再检验一次，是否为404内容
                if (dashVideoPlayBean.code == 0) {
                    DialogUtils.downloadVideoDialog(
                        context,
                        videoBaseBean,
                        bangumiSeasonBean,
                        dashVideoPlayBean,
                        networkService
                    ).show()
                } else if (dashBangumiPlay.code == 0) {
                    DialogUtils.downloadVideoDialog(
                        context,
                        videoBaseBean,
                        bangumiSeasonBean,
                        dashBangumiPlay.toDashVideoPlayBean(),
                        networkService
                    ).show()
                }

                loadDialog.cancel()
            } else {
                AsDialog.init(context).build {
                    config = {
                        title = "止步于此"
                        content =
                            "鉴于你的账户未转正，请前往B站完成答题，否则无法为您提供缓存服务。\n" +
                                    "作者也是B站UP主，见到了许多盗取视频现象，更有甚者缓存番剧后发布内容到其他平台。\n" +
                                    "而你的账户甚至是没有转正的，bilibilias自然不会想提供服务。"
                        positiveButtonText = "知道了"
                        positiveButton = {
                            it.cancel()
                        }
                    }
                }.show()
            }
        }
    }

    /**
     * 显示下载对话框
     */
    private fun showDownloadDialog(
        context: Context,
        videoBaseBean: VideoBaseBean,
        videoPageListData: VideoPageListData,
        dashVideoPlayBean: DashVideoPlayBean,
        networkService: NetworkService
    ) {
        DialogUtils.downloadVideoDialog(
            context,
            videoBaseBean,
            videoPageListData,
            dashVideoPlayBean,
            networkService
        ).show()
    }

    fun downloadDanMu(view: View, videoBaseBean: VideoBaseBean) {
        val context = view.context

        DialogUtils.downloadDMDialog(view.context) { binding ->
            viewModelScope.launchIO {
                val response =
                    HttpUtils.asyncGet("${BilibiliApi.videoDanMuPath}?oid=${(context as AsVideoActivity).cid}")

                when (binding.dialogDlDmTypeRadioGroup.checkedRadioButtonId) {
                    R.id.dialog_dl_dm_ass -> {
                        saveAssDanmaku(
                            context,
                            response.await().body!!.bytes(),
                            videoBaseBean,
                        )
                    }

                    R.id.dialog_dl_dm_xml -> {
                        saveDanmaku(context, response.await().body!!.bytes(), videoBaseBean)
                    }

                    else -> throw Exception("意外的选项")
                }
            }
        }.show()
    }

    private val _danmakuState = MutableStateFlow(AsVideoState())
    val danmakuState = _danmakuState.asStateFlow()
    fun downloadCCAss(view: View, avid: Long, cid: Long) {
        val context = view.context
        val dialogLoad = DialogUtils.loadDialog(context)
        dialogLoad.show()
        viewModelScope.launchIO {
            val bvId = NewVideoNumConversionUtils.av2bv(avid)
            danmakuRepository.getCideoInfoV2(avid, cid).collect { result ->
                when (result) {
                    is Result.Error -> TODO()
                    Result.Loading -> {}
                    is Result.Success -> {
                        _danmakuState.update {
                            it.copy(videoInfoV2 = result.data.data)
                        }
                        launchUI {
                            dialogLoad.cancel()
                            DialogUtils.downloadCCAssDialog(context, result.data.data) {
                                saveCCAss(
                                    bvId,
                                    cid,
                                    result.data.data.name,
                                    it.lan,
                                    it.subtitleUrl,
                                    context
                                )
                            }.show()
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存CC字幕文件
     */
    private fun saveCCAss(
        bvId: String,
        cid: Long,
        title: String,
        lang: String,
        subtitleUrl: String,
        context: Context,
    ) {
        val dialogLoad = DialogUtils.loadDialog(context)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val savePath = sharedPreferences.getString(
            "user_download_save_path",
            context.getExternalFilesDir("download").toString(),
        )
        val fileName = "$savePath/$bvId/${cid}_cc_$lang.ass"
        val assFile = File(fileName)

        val folderFile = File("$savePath/$bvId")
        // 检查是否存在文件夹
        if (!folderFile.exists()) folderFile.mkdirs()

        if (!FileUtils.isFileExists(assFile)) assFile.createNewFile()

        viewModelScope.launchIO {
            val videoCCInfo = http.get(subtitleUrl).body<VideoCCInfo>()

            assFile.writeText(
                CCJsonToAss.jsonToAss(
                    videoCCInfo,
                    title,
                    "1920",
                    "1080",
                    context,
                ),
            )

            launchUI {
                dialogLoad.cancel()
                asToast(context, "下载字幕储存于\n$fileName")
                // 通知下载成功
                Analytics.trackEvent(context.getString(R.string.download_barrage))
            }
        }
    }

    private fun saveAssDanmaku(
        context: AsVideoActivity,
        bytes: ByteArray,
        videoBaseBean: VideoBaseBean,
    ) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val savePath = sharedPreferences.getString(
            "user_download_save_path",
            context.getExternalFilesDir("download").toString(),
        )
        val fileName = "$savePath/${(context.bvid)}/${context.cid}_danmu.ass"
        val assFile = File(fileName)

        val folderFile = File("$savePath/${(context.bvid)}")
        // 检查是否存在文件夹
        if (!folderFile.exists()) folderFile.mkdirs()

        if (!FileUtils.isFileExists(assFile)) assFile.createNewFile()

        val decompressBytes =
            context.decompress(bytes) // 调用解压函数进行解压，返回包含解压后数据的byte数组

        val outputStream = FileOutputStream(assFile)
        decompressBytes.also {
            outputStream.write(it)
            it.clone()
        }

        assFile.writeText(
            DmXmlToAss.xmlToAss(
                assFile.readText(),
                videoBaseBean.data.title + context.cid,
                "1920",
                "1080",
                context,
            ),
        )

        viewModelScope.launchUI {
            asToast(
                context,
                "下载弹幕储存于\n$fileName",
            )
            // 通知下载成功
            Analytics.trackEvent(context.getString(R.string.download_barrage))
        }
    }

    fun saveDanmaku(context: AsVideoActivity, bytes: ByteArray, videoBaseBean: VideoBaseBean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val savePath = sharedPreferences.getString(
            "user_download_save_path",
            context.getExternalFilesDir("download").toString(),
        )

        val bufferedSink: BufferedSink?

        val dest = File("$savePath/${(context.bvid)}/${context.cid}_danmu.xml")
        // 检查是否存在文件夹
        val parentDir = dest.parentFile
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs()
        if (!FileUtils.isFileExists(dest)) {
            File("$savePath/${(context.bvid)}/${context.cid}_danmu.xml").createNewFile()
        }

        val sink = dest.sink() // 打开目标文件路径的sink
        val decompressBytes =
            context.decompress(bytes) // 调用解压函数进行解压，返回包含解压后数据的byte数组
        bufferedSink = sink.buffer()
        decompressBytes.let { bufferedSink.write(it) } // 将解压后数据写入文件（sink）中
        bufferedSink.close()

        viewModelScope.launchUI {
            asToast(
                context,
                "下载弹幕储存于\n$savePath/${(context.bvid)}/${context.cid}_danmu.xml",
            )
            // 通知下载成功
            Analytics.trackEvent(context.getString(R.string.download_barrage))
        }
    }

    /**
     * 点赞视频
     */
    fun likeVideo(hasLike: Boolean, bvid: String) {
        viewModelScope.launch {
            val response = try {
                videoRepository.点赞视频(hasLike, bvid)
            } catch (e: ApiIOException) {
                Napier.d(e) { "走了吗" }
                _effect.send(Event.ShowToast(e.message!!))
                null
            }
            if (response?.success == false) return@launch
            response?.let {
                (asVideoUiState.value as? ViewUiState.Success)?.let {
                    val newTriple = it.viewTriple.copy(true)
                    _effect.send(Event.ToolBarReportChange(newTriple))
                }
            }
        }
    }

    /**
     * 视频投币
     */
    fun videoCoinAdd(bvid: String) {
        viewModelScope.launch {
            val response = try {
                videoRepository.投币视频(bvid)
            } catch (e: ApiIOException) {
                _effect.send(Event.ShowToast(e.message!!))
                null
            }
            response?.let {
                (asVideoUiState.value as? ViewUiState.Success)?.let {
                    val newTriple = it.viewTriple.copy(hasCoins = true)
                    _effect.send(Event.ToolBarReportChange(newTriple))
                }
            }
        }
    }

    /**
     * 加载用户收藏夹
     */
    fun getUserFavorites() {
        viewModelScope.launch {
            val folder =
                userSpaceRepository.查询用户创建的视频收藏夹(loginInfoDataSource.mid.first())
            _effect.send(Event.ShowFavouredDialog(folder))

//            launchUI {
//                if (userCreateCollectionBean.code == 0) {
//                    DialogUtils.loadUserCreateCollectionDialog(
//                        context,
//                        userCreateCollectionBean,
//                        { _, _ ->
//                        },
//                        { selects ->
//                            // 选取完成了收藏文件夹
//                            setCollection(context, selects, aid)
//                        },
//                    ).show()
//                }
        }
    }

    /**
     * 设置收藏夹的ID列表
     * @param selects MutableList<Long>
     */
    private fun setCollection(context: Context, selects: MutableList<Long>, avid: Long) {
        var addMediaIds = ""
        selects.forEachIndexed { index, l ->
            if (index == selects.size) {
                addMediaIds = "$addMediaIds$l"
            }
            addMediaIds = "$addMediaIds,$l"
        }
        addCollection(context, addMediaIds, avid)
    }

    /**
     * 新增收藏夹内容
     * @param addMediaIds String
     */
    private fun addCollection(context: Context, addMediaIds: String, avid: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            val collectionResultBean = networkService.n35(avid.toString(), addMediaIds)

            if (collectionResultBean.code == 0) {
//                context.binding.archiveFavouredBean?.isFavoured = true
//                context.binding.asVideoCollectionBt.isSelected = true
            } else {
                asToast(context, "收藏失败${collectionResultBean.code}")
            }
        }
    }
}

private suspend fun viewDetailUiState(
    bvid: String,
    videoRepository: VideoRepository,
    userRepository: UserRepository,
    getViewTripleUseCase: GetViewTripleUseCase,
    getDmDataWriteFileUseCase: GetDmDataWriteFileUseCase
): Flow<ViewUiState> {
    val detailFlow = flow { emit(videoRepository.获取视频详细信息(bvid)) }
    val cardFlow =
        detailFlow.map { getDmDataWriteFileUseCase(it.cid); userRepository.用户名片信息(it.owner.mid) }
    val tripleFlow = getViewTripleUseCase.invoke(bvid)
    return combine(detailFlow, cardFlow, tripleFlow, ::Triple).asResult().map { result ->
        when (result) {
            is Result.Error -> ViewUiState.Loading
            Result.Loading -> ViewUiState.Loading
            is Result.Success -> {
                val (viewDetail, card, viewTriple) = result.data
                ViewUiState.Success(viewDetail, card, viewTriple)
            }
        }
    }
}
