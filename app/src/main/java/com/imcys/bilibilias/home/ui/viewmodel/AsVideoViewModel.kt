package com.imcys.bilibilias.home.ui.viewmodel

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.config.UserInfoRepository
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.model.video.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.UserSpaceInformation
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.model.video.VideoPageListData
import com.imcys.bilibilias.common.base.repository.UserRepository
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.danmaku.change.DmXmlToAss
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.*
import com.microsoft.appcenter.analytics.Analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * 解析视频的ViewModel
 */
@HiltViewModel
class AsVideoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val videoRepository: VideoRepository
) : BaseViewModel() {

    private val mid = UserInfoRepository.mid
    private val _videoUiState = MutableStateFlow(VideoUiSate())
    val videoUiState = _videoUiState.asStateFlow()

    /**
     * 缓存视频
     * @param videoDetails VideoBaseBean
     * @param videoPageListData VideoPageListData
     */
    fun downloadVideo(
        view: View,
        videoDetails: VideoDetails,
        videoPageListData: VideoPageListData,
    ) {
        val context = view.context
        val loadDialog = DialogUtils.loadDialog(context).apply { show() }

        viewModelScope.launchIO {
            if ((context as AsVideoActivity).userSpaceInformation.level >= 2) {
                val dashVideoPlayBean = KtHttpUtils.addHeader(
                    COOKIE,
                    BaseApplication.dataKv.decodeString(COOKIES, "")!!,
                )
                    .addHeader(REFERER, BILIBILI_URL)
                    .asyncGet<DashVideoPlayBean>(
                        "${BilibiliApi.videoPlayPath}?bvid=${context.bvid}&cid=${context.cid}&qn=64&fnval=4048&fourk=1"
                    )
                // 这里再检验一次，是否为404内容
                loadDialog.cancel()
                launchUI {
                    if (dashVideoPlayBean.videoCodecid == 0) {
                        DialogUtils.downloadVideoDialog(
                            context,
                            videoDetails,
                            videoPageListData,
                            dashVideoPlayBean,
                        ).show()
                    }
                }
            } else {
                launchUI {
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
    }

    /**
     * 缓存番剧
     * @param videoDetails VideoBaseBean
     * @param bangumiSeasonBean BangumiSeasonBean
     */
    fun downloadBangumiVideo(
        view: View,
        videoDetails: VideoDetails,
        bangumiSeasonBean: Bangumi,
    ) {
        val context = view.context

        val loadDialog = DialogUtils.loadDialog(context).apply { show() }

        viewModelScope.launchIO {
            if ((context as AsVideoActivity).userSpaceInformation.level >= 2) {
                val dashVideoPlayBean =
                    KtHttpUtils.addHeader(
                        COOKIE,
                        BaseApplication.dataKv.decodeString(COOKIES, "")!!,
                    )
                        .addHeader(REFERER, BILIBILI_URL)
                        .asyncGet<DashVideoPlayBean>(
                            "${BilibiliApi.videoPlayPath}?bvid=${context.bvid}&cid=${context.cid}&qn=64&fnval=4048&fourk=1"
                        )
                loadDialog.cancel()
                launchUI {
                    if (dashVideoPlayBean.videoCodecid == 0) {
                        DialogUtils.downloadVideoDialog(
                            context,
                            videoDetails,
                            bangumiSeasonBean,
                            dashVideoPlayBean,
                        ).show()
                    }
                }
            } else {
                launchUI {
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
    }

    fun downloadDanMu(view: View, videoDetails: VideoDetails) {
        val context = view.context

        DialogUtils.downloadDMDialog(view.context, videoDetails) { binding ->
            viewModelScope.launchIO {
                val response =
                    HttpUtils.asyncGet("${BilibiliApi.videoDanMuPath}?oid=${(context as AsVideoActivity).cid}")

                when (binding.dialogDlDmTypeRadioGroup.checkedRadioButtonId) {
                    R.id.dialog_dl_dm_ass -> {
                        saveAssDanmaku(
                            context,
                            response.await().body!!.bytes(),
                            videoDetails,
                        )
                    }

                    R.id.dialog_dl_dm_xml -> {
                        saveDanmaku(context, response.await().body!!.bytes(), videoDetails)
                    }

                    else -> throw Exception("意外的选项")
                }
            }
        }.show()
    }

    private fun saveAssDanmaku(
        context: AsVideoActivity,
        bytes: ByteArray,
        videoDetails: VideoDetails,
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

        assFile.writeText(
            DmXmlToAss.xmlToAss(
                assFile.readText(),
                videoDetails.title + context.cid,
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

    fun saveDanmaku(context: AsVideoActivity, bytes: ByteArray, videoDetails: VideoDetails) {
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

        bufferedSink = sink.buffer()
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
     * @param bvid String aid
     */
    fun likeVideo(view: View, bvid: String) {
        val context = view.context

        viewModelScope.launchIO {
            val likeVideoBean =
                KtHttpUtils.addHeader(
                    COOKIE,
                    BaseApplication.dataKv.decodeString(COOKIES, "")!!,
                )
                    .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "")!!)
                    .addParam("like", "1")
                    .addParam("bvid", bvid)
                    .asyncPost<LikeVideoBean>(BilibiliApi.videLikePath)
            // (context as AsVideoActivity).binding.archiveHasLikeBean?.data == 0
            if (true) {
                launchUI {
                    when (likeVideoBean.code) {
                        0 -> {
                            // context.binding.archiveHasLikeBean?.data = 1
                            // context.binding.asVideoLikeBt.isSelected = true
                        }

                        65006 -> {
                            cancelLikeVideo(view, bvid)
                        }

                        else -> {
                            asToast(context, likeVideoBean.message)
                        }
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
            val likeVideoBean =
                KtHttpUtils.addHeader(
                    COOKIE,
                    BaseApplication.dataKv.decodeString(COOKIES, "")!!,
                )
                    .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "") ?: "")
                    .addParam("like", "2")
                    .addParam("bvid", bvid)
                    .asyncPost<LikeVideoBean>(BilibiliApi.videLikePath)

            launchUI {
                when (likeVideoBean.code) {
                    0 -> {
                        (context as AsVideoActivity).binding.apply {
                            // archiveHasLikeBean?.data = 0
                            // asVideoLikeBt.isSelected = false
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
            KtHttpUtils
                .addHeader(COOKIE, BaseApplication.dataKv.decodeString(COOKIES, "")!!)
                .addParam("bvid", bvid)
                .addParam("multiply", "2")
                .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "")!!)
                .asyncPost<VideoCoinAddBean>(BilibiliApi.videoCoinAddPath)

            launchUI {
                // (context as AsVideoActivity).binding.archiveCoinsBean?.multiply = 2
                // context.binding.asVideoThrowBt.isSelected = true
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
                val userCreateCollectionBean =
                    KtHttpUtils.addHeader(
                        COOKIE,
                        BaseApplication.dataKv.decodeString(COOKIES, "")!!,
                    )
                        .asyncGet<UserCreateCollectionBean>("?up_mid=" + asUser.mid)

                launchUI {
                    if (userCreateCollectionBean.count == 0) {
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
        // Only show AuthenticationMethod toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, context.getString(R.string.Copied), Toast.LENGTH_SHORT).show()
        }

        return true
    }

    /**
     * 设置收藏夹的ID列表
     * @param selects MutableList<Long>
     */
    private fun setCollection(context: AsVideoActivity, selects: MutableList<Long>, avid: Long) {
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
            val collectionResultBean =
                KtHttpUtils.addHeader(COOKIE, asUser.cookie)
                    .addParam("rid", avid.toString())
                    .addParam("add_media_ids", addMediaIds)
                    .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "")!!)
                    .addParam("type", "2")
                    .asyncPost<CollectionResultBean>(BilibiliApi.videoCollectionSetPath)

            if (collectionResultBean.code == 0) {
                // context.binding.archiveFavouredBean?.isFavoured = true
                // context.binding.asVideoCollectionBt.isSelected = true
            } else {
                asToast(context, "收藏失败${collectionResultBean.code}")
            }
        }
    }

    suspend fun getUserData(): UserSpaceInformation = withContext(Dispatchers.IO) {
        userRepository.getUserSpaceDetails(mid)
    }

    fun getVideoData(bvid: String) {
        launchIO {
            videoRepository.getVideoDetailsByBvid(bvid)
        }
    }

    fun loadBangumiVideoList(epID: String) {
        launchIO {
            val 剧集基本信息 = videoRepository.get剧集基本信息(epID)
            Timber.d(剧集基本信息.toString())
            _videoUiState.update {
                it.copy(
                    // cid = 剧集基本信息.episode[0].cid,
                    // totalEpisodes = 剧集基本信息.total,
                    // episodes = 剧集基本信息.episodes
                )
            }
        }
    }

    suspend fun loadDanmakuFlameMaster(cid: String): ByteArray {
        return TODO()
    }

    suspend fun loadUserCardData() = userRepository.get用户名片信息(mid)
    suspend fun archiveHasLike(bvid: String) = videoRepository.hasLike(bvid)
    suspend fun archiveFavoured(bvid: String) = videoRepository.hasCollection(bvid)
    suspend fun archiveCoins(bvid: String) = videoRepository.hasCoins(bvid)

    suspend fun getDash(
        bvid: String,
        cid: Long,
        quality: Int,
        format: Int,
        allow4KVideo: Int
    ) = videoRepository.getDashVideoStream(bvid, cid, quality, format)

    fun get番剧视频流(epID: String, cid: Long) {
        launchIO {
            val 番剧 = videoRepository.get番剧视频流(epID, cid)
            Timber.d(番剧.durl[0].url)
            _videoUiState.update {
                it.copy(播放地址 = 番剧.durl[0].url)
            }
        }
    }
}

data class VideoUiSate(
    // 视频的基本信息
    val 播放地址: String = "",
    val title: String = "",
    val cid: Long = 0,
    val pic: String = "",
    val desc: String = "",
    val descV2: String? = null,
    // 仅番剧或影视视频存在此字段用于番剧&影视的av/bv->ep
    val redirectUrl: String? = null,
    // 视频作者信息
    val ownerFace: String = "",
    val ownerName: String = "",

    val totalEpisodes: Int = 0,
    val episodes: List<Bangumi.Result.Episode> = emptyList(),
)
