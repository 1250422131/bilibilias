package com.imcys.bilibilias.home.ui.viewmodel

import android.annotation.*
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.*
import android.view.*
import android.widget.*
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.*
import androidx.preference.*
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.extend.toAsDownloadSavePath
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.base.utils.file.hasSubDirectory
import com.imcys.bilibilias.common.network.danmaku.*
import com.imcys.bilibilias.danmaku.change.*
import com.imcys.bilibilias.home.ui.activity.*
import com.imcys.bilibilias.home.ui.activity.user.UserInfoActivity
import com.imcys.bilibilias.home.ui.model.*
import com.liulishuo.okdownload.OkDownloadProvider
import com.microsoft.appcenter.analytics.*
import dagger.hilt.android.lifecycle.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okio.*
import java.io.*
import javax.inject.*

/**
 * 解析视频的ViewModel
 */

@HiltViewModel
class AsVideoViewModel @Inject constructor(private val danmakuRepository: DanmakuRepository) :
    ViewModel() {

    @Inject
    lateinit var http: HttpClient

    @Inject
    lateinit var networkService: NetworkService

    fun toUserPage(view: View, mid: String) {
        UserInfoActivity.actionStart(view.context, mid.toLong())
    }

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
            (context as AsVideoActivity)
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
            context as AsVideoActivity
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
        }
    }

    /**
     * 下载视频弹幕对话框
     */
    fun downloadVideoDanMu(
        view: View,
        videoBaseBean: VideoBaseBean,
        videoPageListData: VideoPageListData,
    ) {
        val context = view.context as AsVideoActivity

        DialogUtils.downloadDMDialog(
            view.context,
            videoBaseBean,
            videoPageListData
        ) { binding, selectVideo ->

            val loadDialog = DialogUtils.loadProgressDialog(context)
            loadDialog.show()
            val titleView = loadDialog.findViewById<TextView>(R.id.dialog_load_tip)
            val progressBar = loadDialog.findViewById<ProgressBar>(R.id.dialog_load_progressBar)
            titleView?.text = "正在加载"
            progressBar?.max = selectVideo.size
            progressBar?.progress = 1
            viewModelScope.launchIO {
                selectVideo.forEachIndexed { index, dataBean ->
                    launchUI {
                        progressBar?.progress = index
                    }
                    val videoPageListData = dataBean
                    val fileType = when (binding.dialogDlDmTypeRadioGroup.checkedRadioButtonId) {
                        R.id.dialog_dl_dm_ass -> "ass"

                        R.id.dialog_dl_dm_xml -> "xml"

                        else -> throw Exception("意外的选项")
                    }
                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context)
                    val inputString =
                        sharedPreferences.getString(
                            "user_download_file_name_editText",
                            "{BV}/{FILE_TYPE}/{P}_{P_TITLE}_{CID}.{FILE_TYPE}",
                        )
                            .toString()
                    val savePath = inputString.toAsDownloadSavePath(
                        context,
                        videoBaseBean.data.aid.toString(),
                        videoBaseBean.data.bvid,
                        videoPageListData.part,
                        dataBean.cid.toString(),
                        fileType,
                        dataBean.page.toString(),
                        videoBaseBean.data.title,
                        fileType,
                    )
                    val danmakuByte = networkService.getDanmuBytes(dataBean.cid)

                    when (binding.dialogDlDmTypeRadioGroup.checkedRadioButtonId) {
                        R.id.dialog_dl_dm_ass -> {
                            saveAssDanmaku(context, danmakuByte, videoBaseBean, savePath)
                        }

                        R.id.dialog_dl_dm_xml -> {
                            saveDanmaku(context, danmakuByte, videoBaseBean, savePath)
                        }
                    }
                }

                //　关闭对话框
                launchUI {
                    loadDialog.cancel()
                }
            }
        }.show()
    }

    /**
     * 下载番剧弹幕对话框
     */
    fun downloadBangumiDanMu(
        view: View,
        videoBaseBean: VideoBaseBean,
        videoPageListData: BangumiSeasonBean,
    ) {
        val context = view.context as AsVideoActivity
        DialogUtils.downloadDMDialog(
            view.context,
            videoBaseBean,
            videoPageListData
        ) { binding, selectVideo ->
            val loadDialog = DialogUtils.loadProgressDialog(context)
            loadDialog.show()
            val titleView = loadDialog.findViewById<TextView>(R.id.dialog_load_tip)
            val progressBar = loadDialog.findViewById<ProgressBar>(R.id.dialog_load_progressBar)
            titleView?.text = "正在加载"
            progressBar?.max = selectVideo.size
            progressBar?.progress = 1

            viewModelScope.launchIO {

                selectVideo.forEachIndexed { index, dataBean ->
                    launchUI {
                        progressBar?.progress = index
                    }
                    val fileType = when (binding.dialogDlDmTypeRadioGroup.checkedRadioButtonId) {
                        R.id.dialog_dl_dm_ass -> "ass"

                        R.id.dialog_dl_dm_xml -> "xml"

                        else -> throw Exception("意外的选项")
                    }
                    // 读取命名规则
                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val inputString =
                        sharedPreferences.getString(
                            "user_download_file_name_editText",
                            "{BV}/{FILE_TYPE}/{P}_{P_TITLE}_{CID}.{FILE_TYPE}",
                        )
                            .toString()

                    // 扩展函数 -> 把下载地址换出来
                    val savePath = inputString.toAsDownloadSavePath(
                        context,
                        videoBaseBean.data.aid.toString(),
                        videoBaseBean.data.bvid,
                        dataBean.long_title,
                        dataBean.cid.toString(),
                        fileType,
                        dataBean.title,
                        videoBaseBean.data.title,
                        fileType,
                    )

                    val danmakuByte = networkService.getDanmuBytes(dataBean.cid)

                    when (binding.dialogDlDmTypeRadioGroup.checkedRadioButtonId) {
                        R.id.dialog_dl_dm_ass -> {
                            saveAssDanmaku(context, danmakuByte, videoBaseBean, savePath)
                        }

                        R.id.dialog_dl_dm_xml -> {
                            saveDanmaku(context, danmakuByte, videoBaseBean, savePath)
                        }
                    }

                }

                launchUI {
                    loadDialog.cancel()
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
            moveFileToDlUriPath("$savePath/$bvId/${cid}_cc_$lang.ass")

            launchUI {
                dialogLoad.cancel()
                asToast(context, "下载字幕储存于：存储目录/$bvId/${cid}_cc_$lang.ass")
                // 通知下载成功
                Analytics.trackEvent(context.getString(R.string.download_barrage))
            }
        }
    }

    private suspend fun saveAssDanmaku(
        context: AsVideoActivity,
        bytes: ByteArray,
        videoBaseBean: VideoBaseBean,
        savePath: String,
    ) = withContext(Dispatchers.IO) {
        val assFile = File(savePath)

        // 确保父目录存在（递归创建所有必要的目录）
        if (assFile.parentFile?.exists() != true) {
            assFile.parentFile?.mkdirs() // 递归创建所有缺失的父目录
        }

        // 清空文件内容或创建文件
        if (assFile.exists()) {
            assFile.writeText("") // 清空文件内容
        } else {
            assFile.createNewFile() // 创建文件
        }

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
        moveFileToDlUriPath(savePath)
        launchUI {
            // 通知下载成功
            asToast(context, "下载完成")
            Analytics.trackEvent(context.getString(R.string.download_barrage))
        }
    }

    private suspend fun saveDanmaku(
        context: AsVideoActivity,
        bytes: ByteArray,
        videoBaseBean: VideoBaseBean,
        savePath: String,
    ) = withContext(Dispatchers.IO) {

        val bufferedSink: BufferedSink?
        val dest = File(savePath)


        // 确保父目录存在（递归创建所有必要的目录）
        if (dest.parentFile?.exists() != true) {
            dest.parentFile?.mkdirs() // 递归创建所有缺失的父目录
        }

        // 清空文件内容或创建文件
        if (dest.exists()) {
            dest.writeText("") // 清空文件内容
        } else {
            dest.createNewFile() // 创建文件
        }

        val sink = dest.sink() // 打开目标文件路径的sink
        val decompressBytes =
            context.decompress(bytes) // 调用解压函数进行解压，返回包含解压后数据的byte数组
        bufferedSink = sink.buffer()
        decompressBytes.let { bufferedSink.write(it) } // 将解压后数据写入文件（sink）中
        bufferedSink.close()

        moveFileToDlUriPath(savePath)
        launchUI {
            // 通知下载成功
            asToast(context, "下载完成")
            Analytics.trackEvent(context.getString(R.string.download_barrage))
        }
    }

    /**
     * 点赞视频
     * @param bvid String aid
     */
    fun likeVideo(view: View, bvid: String) {
        val context = view.context

        viewModelScope.launchUI {
            val likeVideoBean = networkService.videoLike(bvid)

            if ((context as AsVideoActivity).binding.archiveHasLikeBean?.data == 0) {
                when (likeVideoBean.code) {
                    0 -> {
                        context.binding.archiveHasLikeBean?.data = 1
                        context.binding.asVideoLikeBt.isSelected = true
                    }

                    65006 -> {
                        cancelLikeVideo(view, bvid)
                    }

                    else -> {
                        asToast(context, likeVideoBean.message)
                    }
                }
            } else {
                cancelLikeVideo(view, bvid)
            }
        }
    }

    /**
     * 取消对视频的点赞
     * @param bvid String
     */
    private fun cancelLikeVideo(view: View, bvid: String) {
        val context = view.context

        viewModelScope.launchIO {
            val likeVideoBean = networkService.n32(bvid)

            launchUI {
                when (likeVideoBean.code) {
                    0 -> {
                        (context as AsVideoActivity).binding.apply {
                            archiveHasLikeBean?.data = 0
                            asVideoLikeBt.isSelected = false
                        }
                    }

                    65004 -> {
                        likeVideo(view, bvid)
                    }

                    else -> {
                        asToast(context, likeVideoBean.message)
                    }
                }
            }
        }
    }

    /**
     * 视频投币
     * @param bvid String
     */
    fun videoCoinAdd(view: View, bvid: String) {
        val context = view.context

        viewModelScope.launchIO {
            networkService.n33(bvid)

            launchUI {
                (context as AsVideoActivity).binding.archiveCoinsBean?.multiply = 2
                context.binding.asVideoThrowBt.isSelected = true
            }
        }
    }

    /**
     * 加载用户收藏夹
     */
    @SuppressLint("NotifyDataSetChanged")
    fun loadCollectionView(view: View, avid: Long) {
        val context = view.context
        (context as AsVideoActivity).binding.apply {
            viewModelScope.launchIO {
                val userCreateCollectionBean = networkService.n34()

                launchUI {
                    if (userCreateCollectionBean.code == 0) {
                        DialogUtils.loadUserCreateCollectionDialog(
                            context,
                            userCreateCollectionBean,
                            { _, _ ->
                            },
                            { selects ->
                                // 选取完成了收藏文件夹
                                setCollection(context, selects, avid)
                            },
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * 复制内容
     * @param inputStr String
     */
    fun addClipboardMessage(view: View, inputStr: String): Boolean {
        val context = view.context

        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        // When setting the clip board text.
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", inputStr))
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, context.getString(R.string.Copied), Toast.LENGTH_SHORT)
                .show()
        }

        return true
    }

    /**
     * 复制内容
     * @param inputStr String
     */
    fun addVideoTipClipboardMessage(view: View, inputStr: String): Boolean {
        val context = view.context

        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        // When setting the clip board text.
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", inputStr))
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, context.getString(R.string.Copied), Toast.LENGTH_SHORT)
                .show()
        }

        return true
    }

    /**
     * 设置收藏夹的ID列表
     * @param selects MutableList<Long>
     */
    private fun setCollection(
        context: AsVideoActivity,
        selects: MutableList<Long>,
        avid: Long,
    ) {
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
    private fun addCollection(context: AsVideoActivity, addMediaIds: String, avid: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            val collectionResultBean = networkService.n35(avid.toString(), addMediaIds)

            if (collectionResultBean.code == 0) {
                context.binding.archiveFavouredBean?.isFavoured = true
                context.binding.asVideoCollectionBt.isSelected = true
            } else {
                asToast(context, "收藏失败${collectionResultBean.code}")
            }
        }
    }

    private fun moveFileToDlUriPath(oldPath: String) {
        val result = runCatching {
            launchIO {
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
                val saveUriPath = sharedPreferences.getString(
                    "user_download_save_uri_path",
                    null,
                )

                if (saveUriPath != null) {
                    var dlFileDocument = DocumentFile.fromTreeUri(
                        OkDownloadProvider.context,
                        Uri.parse(saveUriPath)
                    )!!

                    val docList = oldPath.replace(
                        "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download/",
                        ""
                    ).split("/")

                    docList.forEachIndexed { index, name ->
                        // 是不是最后尾部
                        if (index != docList.size - 1) {
                            dlFileDocument = if (!dlFileDocument.hasSubDirectory(name)) {
                                dlFileDocument.createDirectory(name)!!
                            } else {
                                dlFileDocument.findFile(name)!!
                            }
                        } else {
                            dlFileDocument =
                                dlFileDocument.createFile(
                                    "application/${name.split(".").last()}",
                                    name
                                )!!
                            val copyResult = AppFilePathUtils.copySafFile(
                                oldPath,
                                dlFileDocument.uri,
                                OkDownloadProvider.context
                            )

                            if (copyResult) {
                                FileUtils.deleteFile(oldPath)
                            } else {
                                launchUI {
                                    asToast(
                                        OkDownloadProvider.context,
                                        "移动失败，文件会被保留在原路径"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result.isFailure) {
            launchUI {
                asToast(
                    OkDownloadProvider.context,
                    "移动失败，文件会被保留在初始路径，请在删除后重新下载"
                )
            }
        }
    }
}
