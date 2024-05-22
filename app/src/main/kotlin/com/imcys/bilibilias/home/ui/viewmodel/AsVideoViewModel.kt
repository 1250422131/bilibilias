package com.imcys.bilibilias.home.ui.viewmodel

import android.annotation.*
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.os.*
import android.view.*
import android.widget.*
import androidx.lifecycle.*
import androidx.preference.*
import com.imcys.asbottomdialog.bottomdialog.*
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.danmaku.change.*
import com.imcys.bilibilias.home.ui.activity.*
import com.imcys.bilibilias.home.ui.model.*
import com.microsoft.appcenter.analytics.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okio.*
import java.io.*
import javax.inject.*

/**
 * 解析视频的ViewModel
 */

@HiltViewModel
class AsVideoViewModel @Inject constructor() :
    ViewModel() {

    fun toUserPage(view: View, mid: String) {

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


        viewModelScope.launchUI {
            if ((context as AsVideoActivity).userBaseBean.data.level >= 2) {

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



        viewModelScope.launchUI {

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
    ) {
    }

    fun downloadDanMu(view: View, videoBaseBean: VideoBaseBean) {
    }

    fun downloadCCAss(view: View, avid: Long, cid: Long) {

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

//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

//        val savePath = sharedPreferences.getString(
//            "user_download_save_path",
//            context.getExternalFilesDir("download").toString(),
//        )
        val fileName = "$/$bvId/${cid}_cc_$lang.ass"
        val assFile = File(fileName)

        val folderFile = File("$/$bvId")
        // 检查是否存在文件夹
        if (!folderFile.exists()) folderFile.mkdirs()

        if (!FileUtils.isFileExists(assFile)) assFile.createNewFile()

        viewModelScope.launchIO {
            launchUI {

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
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

//        val savePath = sharedPreferences.getString(
//            "user_download_save_path",
//            context.getExternalFilesDir("download").toString(),
//        )
        val fileName = "/${(context.bvid)}/${context.cid}_danmu.ass"
        val assFile = File(fileName)

        val folderFile = File("$/${(context.bvid)}")
        // 检查是否存在文件夹
        if (!folderFile.exists()) folderFile.mkdirs()

        if (!FileUtils.isFileExists(assFile)) assFile.createNewFile()

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
            // 通知下载成功
            Analytics.trackEvent(context.getString(R.string.download_barrage))
        }
    }

    fun saveDanmaku(context: AsVideoActivity, bytes: ByteArray, videoBaseBean: VideoBaseBean) {
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

//        val savePath = sharedPreferences.getString(
//            "user_download_save_path",
//            context.getExternalFilesDir("download").toString(),
//        )

        val dest = File("$${(context.bvid)}/${context.cid}_danmu.xml")
        // 检查是否存在文件夹
        val parentDir = dest.parentFile
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs()
        if (!FileUtils.isFileExists(dest)) {
            File("$/${(context.bvid)}/${context.cid}_danmu.xml").createNewFile()
        }

        viewModelScope.launchUI {
            // 通知下载成功
            Analytics.trackEvent(context.getString(R.string.download_barrage))
        }
    }

    /**
     * 点赞视频
     * @param bvid String aid
     */
    fun likeVideo(view: View, bvid: String) {

    }

    /**
     * 视频投币
     * @param bvid String
     */
    fun videoCoinAdd(view: View, bvid: String) {
    }

    /**
     * 加载用户收藏夹
     */
    @SuppressLint("NotifyDataSetChanged")
    fun loadCollectionView(view: View, avid: Long) {

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
    }
}
