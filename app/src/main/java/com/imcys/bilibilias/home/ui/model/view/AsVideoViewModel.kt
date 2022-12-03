package com.imcys.bilibilias.home.ui.model.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Toast
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.utils.HttpUtils

/**
 * 解析视频的ViewModel
 */
class AsVideoViewModel(val context: Context, private val asVideoBinding: ActivityAsVideoBinding) {


    fun downloadVideo(
        videoBaseBean: VideoBaseBean,
        videoPageListData: VideoPageListData,
        videoPlayBean: VideoPlayBean,
    ) {
        DialogUtils.downloadVideoDialog(context, videoBaseBean, videoPageListData,videoPlayBean).show()
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
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, it.code.toString(), Toast.LENGTH_SHORT).show()

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


    private fun getEmphasizeColor(): ColorStateList {
        return ColorStateList.valueOf(Color.parseColor("#FB7299"))
    }


}