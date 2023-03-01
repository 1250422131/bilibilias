package com.imcys.bilibilias.home.ui.model.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.microsoft.appcenter.analytics.Analytics
import kotlinx.coroutines.*
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File

/**
 * 解析视频的ViewModel
 */
class AsVideoViewModel(
    val context: Context, private val asVideoBinding: ActivityAsVideoBinding,
) : ViewModel() {


    /**
     * 缓存视频
     * @param videoBaseBean VideoBaseBean
     * @param videoPageListData VideoPageListData
     */
    fun downloadVideo(
        videoBaseBean: VideoBaseBean,
        videoPageListData: VideoPageListData,
    ) {
        val loadDialog = DialogUtils.loadDialog(context).apply { show() }
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {

            if ((context as AsVideoActivity).userBaseBean.data.level > 2) {
                val dashVideoPlayBean = KtHttpUtils.addHeader("cookie", context.asUser.cookie)
                    .addHeader("referer", "https://www.bilibili.com")
                    .asyncGet<DashVideoPlayBean>("${BilibiliApi.videoPlayPath}?bvid=${context.bvid}&cid=${context.cid}&qn=64&fnval=4048&fourk=1")
                //这里再检验一次，是否为404内容
                loadDialog.cancel()
                launch(Dispatchers.Main) {
                    if (dashVideoPlayBean.code == 0) DialogUtils.downloadVideoDialog(
                        context,
                        videoBaseBean,
                        videoPageListData,
                        dashVideoPlayBean
                    ).show()
                }

            } else {
                AsDialog.build {
                    config = {
                        title = "止步于此"
                        content = "鉴于你的账户未转正，请前往B站完成答题，否则无法为您提供缓存服务。\n" +
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
        videoBaseBean: VideoBaseBean,
        bangumiSeasonBean: BangumiSeasonBean,
    ) {
        val loadDialog = DialogUtils.loadDialog(context).apply { show() }

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {

            if ((context as AsVideoActivity).userBaseBean.data.level > 2) {
                val dashVideoPlayBean =
                    KtHttpUtils.addHeader("cookie", (context as AsVideoActivity).asUser.cookie)
                        .addHeader("referer", "https://www.bilibili.com")
                        .asyncGet<DashVideoPlayBean>("${BilibiliApi.videoPlayPath}?bvid=${(context as AsVideoActivity).bvid}&cid=${context.cid}&qn=64&fnval=4048&fourk=1")
                loadDialog.cancel()
                launch(Dispatchers.Main) {
                    if (dashVideoPlayBean.code == 0) DialogUtils.downloadVideoDialog(
                        context,
                        videoBaseBean,
                        bangumiSeasonBean,
                        dashVideoPlayBean
                    ).show()
                }

            } else {
                AsDialog.build {
                    config = {
                        title = "止步于此"
                        content = "鉴于你的账户未转正，请前往B站完成答题，否则无法为您提供缓存服务。\n" +
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


    fun downloadDanMu(videoBaseBean: VideoBaseBean) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch(Dispatchers.IO) {

            val response =
                HttpUtils.asyncGet("${BilibiliApi.videoDanMuPath}?oid=${(context as AsVideoActivity).cid}")

            saveDanmaku(response.await().body!!.bytes(), videoBaseBean.data.cid)

        }

    }


    fun saveDanmaku(bytes: ByteArray, cid: Int) {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val savePath = sharedPreferences.getString(
            "user_download_save_path",
            context.getExternalFilesDir("download").toString()
        )

        val bufferedSink: BufferedSink?
        val dest = File("${savePath}/${cid}_danmu.xml")
        if (!FileUtils.isFileExists(dest)) dest.createNewFile()
        val sink = dest.sink() //打开目标文件路径的sink
        val decompressBytes =
            (context as AsVideoActivity).decompress(bytes) //调用解压函数进行解压，返回包含解压后数据的byte数组
        bufferedSink = sink.buffer()
        decompressBytes?.let { bufferedSink.write(it) } //将解压后数据写入文件（sink）中
        bufferedSink.close()

        coroutineScope.launch(Dispatchers.Main) {
            asToast(context, "下载弹幕储存于\n${savePath}/${cid}_danmu.xml")
            //通知下载成功
            Analytics.trackEvent("下载弹幕")
        }

    }

    /**
     * 点赞视频
     * @param bvid String aid
     */
    fun likeVideo(view: View, bvid: String) {

        if (asVideoBinding.archiveHasLikeBean?.data == 0) {
            HttpUtils
                .addHeader("cookie", (context as AsVideoActivity).asUser.cookie)
                .addParam("csrf", context.asUser.biliJct)
                .addParam("like", "1")
                .addParam("bvid", bvid)
                .post(BilibiliApi.videLikePath, LikeVideoBean::class.java) {
                    when (it.code) {
                        0 -> {
                            asVideoBinding.archiveHasLikeBean?.data = 1
                            asVideoBinding.asVideoLikeBt.isSelected = true
                        }
                        65006 -> {
                            cancelLikeVideo(view, bvid)
                        }
                        else -> {
                            asToast(context, it.message)
                        }
                    }
                }
        } else {
            cancelLikeVideo(view, bvid)
        }


    }

    /**
     * 取消对视频的点赞
     * @param bvid String
     */
    private fun cancelLikeVideo(view: View, bvid: String) {
        HttpUtils
            .addHeader("cookie", (context as AsVideoActivity).asUser.cookie)
            .addParam("csrf", context.asUser.biliJct)
            .addParam("like", "2")
            .addParam("bvid", bvid)
            .post(BilibiliApi.videLikePath, LikeVideoBean::class.java) {
                when (it.code) {
                    0 -> {
                        asVideoBinding.archiveHasLikeBean?.data = 0
                        asVideoBinding.asVideoLikeBt.isSelected = false
                    }
                    65004 -> {
                        likeVideo(view, bvid)
                    }
                    else -> {
                        asToast(context, it.message)
                    }
                }
            }
    }


    /**
     * 视频投币
     * @param bvid String
     */
    fun videoCoinAdd(view: View, bvid: String) {
        HttpUtils
            .addHeader("cookie", (context as AsVideoActivity).asUser.cookie)
            .addParam("bvid", bvid)
            .addParam("multiply", "2")
            .addParam("csrf", context.asUser.biliJct)
            .post(BilibiliApi.videoCoinAddPath, VideoCoinAddBean::class.java) {
                asVideoBinding.archiveCoinsBean?.data?.multiply = 2
                asVideoBinding.asVideoThrowBt.isSelected = true

            }
    }


    /**
     * 加载用户收藏夹
     */
    @SuppressLint("NotifyDataSetChanged")
    fun loadCollectionView(avid: Int) {
        asVideoBinding.apply {
            HttpUtils.addHeader("cookie", (context as AsVideoActivity).asUser.cookie)
                .get(
                    BilibiliApi.userCreatedScFolderPath + "?up_mid=" + context.asUser.mid,
                    UserCreateCollectionBean::class.java
                ) {
                    if (it.code == 0) {
                        DialogUtils.loadUserCreateCollectionDialog(context as Activity,
                            it, { _, _ ->
                            }, { selects ->
                                //选取完成了收藏文件夹
                                setCollection(selects, avid)
                            }).show()
                    }
                }
        }

    }

    /**
     * 复制内容
     * @param inputStr String
     */
    fun addClipboardMessage(inputStr: String): Boolean {
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        // When setting the clip board text.
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", inputStr))
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show()

        return true

    }


    /**
     * 设置收藏夹的ID列表
     * @param selects MutableList<Long>
     */
    private fun setCollection(selects: MutableList<Long>, avid: Int) {

        var addMediaIds = ""
        selects.forEachIndexed { index, l ->
            if (index == selects.size) {
                addMediaIds = "$addMediaIds$l"
            }
            addMediaIds = "$addMediaIds,$l"
        }
        addCollection(addMediaIds, avid)
    }


    /**
     * 新增收藏夹内容
     * @param addMediaIds String
     */
    private fun addCollection(addMediaIds: String, avid: Int) {
        HttpUtils.addHeader("cookie", (context as AsVideoActivity).asUser.cookie)
            .addParam("rid", avid.toString())
            .addParam("add_media_ids", addMediaIds)
            .addParam("csrf", context.asUser.biliJct)
            .addParam("type", "2")
            .post(BilibiliApi.videoCollectionSetPath, CollectionResultBean::class.java) {
                if (it.code == 0) {
                    asVideoBinding.archiveFavouredBean?.data?.isFavoured = true
                    asVideoBinding.asVideoCollectionBt.isSelected = true
                } else {
                    asToast(context, "收藏失败${it.code}")
                }
            }

    }


}