package com.imcys.bilibilias.home.ui.model.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Toast
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asLogD
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.utils.HttpUtils

/**
 * 解析视频的ViewModel
 */
class AsVideoViewModel(val context: Context, private val asVideoBinding: ActivityAsVideoBinding) {


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
     * 点赞视频
     * @param bvid String aid
     */
    fun likeVideo(bvid: String) {
        HttpUtils
            .addHeader("cookie", App.cookies)
            .addParam("csrf", App.biliJct)
            .addParam("like", "1")
            .addParam("bvid", bvid)
            .post(BilibiliApi.videLikePath, LikeVideoBean::class.java) {
                if (it.code == 0){
                    asToast(context, "点赞成功")
                }else{
                    asToast(context, it.message)
                }
                changeLikeButtonToTrue()
            }
    }


    /**
     * 视频投币
     * @param bvid String
     */
    fun videoCoinAdd(bvid: String) {
        HttpUtils
            .addHeader("cookie", App.cookies)
            .addParam("bvid", bvid)
            .addParam("multiply", "2")
            .addParam("csrf", App.biliJct)
            .post(BilibiliApi.videoCoinAddPath, VideoCoinAddBean::class.java) {
                asToast(context, it.message)
                changeCoinAddButtonToTrue()
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
                    changeCollectionButtonToTrue()
                } else {
                    asToast(context, "收藏失败${it.code}")
                }
            }

    }

    //三联按钮状态更新
    private fun changeCollectionButtonToTrue() {
        asVideoBinding.asVideoCollectionBt.imageTintList =
            getEmphasizeColor()
    }

    private fun changeLikeButtonToTrue() {
        asVideoBinding.asVideoLikeBt.imageTintList = getEmphasizeColor()
    }

    private fun changeCoinAddButtonToTrue() {
        asVideoBinding.asVideoLikeBt.imageTintList = getEmphasizeColor()
    }


    @SuppressLint("ResourceAsColor")
    private fun getEmphasizeColor(): ColorStateList {
        return ColorStateList.valueOf(R.color.color_primary)
    }


}