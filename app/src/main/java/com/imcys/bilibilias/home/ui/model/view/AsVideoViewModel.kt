package com.imcys.bilibilias.home.ui.model.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.view.View
import android.widget.Toast
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.common.base.utils.http.HttpUtils

/**
 * 解析视频的ViewModel
 */
class AsVideoViewModel(val context: Context, private val asVideoBinding: ActivityAsVideoBinding) {


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
        HttpUtils.addHeader("cookie", App.cookies).addHeader("referer", "https://www.bilibili.com")
            .get("${BilibiliApi.videoPlayPath}?bvid=${(context as AsVideoActivity).bvid}&cid=${context.cid}&qn=64&fnval=80&fourk=1",
                DashVideoPlayBean::class.java) {
                loadDialog.cancel()
                DialogUtils.downloadVideoDialog(context, videoBaseBean, videoPageListData, it)
                    .show()
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
        HttpUtils.addHeader("cookie", App.cookies).addHeader("referer", "https://www.bilibili.com")
            .get("${BilibiliApi.videoPlayPath}?bvid=${(context as AsVideoActivity).bvid}&cid=${context.cid}&qn=64&fnval=80&fourk=1",
                DashVideoPlayBean::class.java) {
                loadDialog.cancel()
                DialogUtils.downloadVideoDialog(context, videoBaseBean, bangumiSeasonBean, it)
                    .show()
            }


    }


    /**
     * 点赞视频
     * @param bvid String aid
     */
    fun likeVideo(view:View,bvid: String) {

        if (asVideoBinding.archiveHasLikeBean?.data == 0){
            HttpUtils
                .addHeader("cookie", App.cookies)
                .addParam("csrf", App.biliJct)
                .addParam("like", "1")
                .addParam("bvid", bvid)
                .post(BilibiliApi.videLikePath, LikeVideoBean::class.java) {
                    when (it.code) {
                        0 -> {
                            asVideoBinding.archiveHasLikeBean?.data = 1
                            asVideoBinding.asVideoLikeBt.isSelected = true
                        }
                        65006 -> {
                            cancelLikeVideo(view,bvid)
                        }
                        else -> {
                            asToast(context, it.message)
                        }
                    }
                }
        }else{
            cancelLikeVideo(view,bvid)
        }


    }

    /**
     * 取消对视频的点赞
     * @param bvid String
     */
    private fun cancelLikeVideo(view:View,bvid: String){
        HttpUtils
            .addHeader("cookie", App.cookies)
            .addParam("csrf", App.biliJct)
            .addParam("like", "2")
            .addParam("bvid", bvid)
            .post(BilibiliApi.videLikePath, LikeVideoBean::class.java) {
                when (it.code) {
                    0 -> {
                        asVideoBinding.archiveHasLikeBean?.data = 0
                        asVideoBinding.asVideoLikeBt.isSelected = false
                    }
                    65004 -> {
                        likeVideo(view,bvid)
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
    fun videoCoinAdd(view: View,bvid: String) {
        HttpUtils
            .addHeader("cookie", App.cookies)
            .addParam("bvid", bvid)
            .addParam("multiply", "2")
            .addParam("csrf", App.biliJct)
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
            HttpUtils.addHeader("cookie", App.cookies)
                .get(BilibiliApi.userCreatedScFolderPath + "?up_mid=" + App.mid,
                    UserCreateCollectionBean::class.java) {
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
        HttpUtils.addHeader("cookie", App.cookies)
            .addParam("rid", avid.toString())
            .addParam("add_media_ids", addMediaIds)
            .addParam("csrf", App.biliJct)
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

    //三联按钮状态更新
    private fun changeCollectionButtonToTrue() {
        asVideoBinding.asVideoCollectionBt.setColorFilter(R.color.color_primary)
    }

    private fun changeLikeButtonToFalse() {
        asVideoBinding.asVideoCollectionBt.setColorFilter(R.color.black)
    }

    private fun changeLikeButtonToTrue() {
        asVideoBinding.asVideoLikeBt.setColorFilter(R.color.color_primary)
    }

    private fun changeCoinAddButtonToTrue() {
        asVideoBinding.asVideoLikeBt.setColorFilter(R.color.color_primary)
    }


}