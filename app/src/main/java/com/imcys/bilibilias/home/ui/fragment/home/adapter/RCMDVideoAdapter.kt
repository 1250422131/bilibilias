package com.imcys.bilibilias.home.ui.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.databinding.ItemRcmdVideoBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.HomeRCMDVideoBean
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.youth.banner.adapter.BannerAdapter

class RCMDVideoAdapter(
    val context: Context,
    val datas: MutableList<HomeRCMDVideoBean.DataBean.ItemBean>,
) :
    BannerAdapter<HomeRCMDVideoBean.DataBean.ItemBean, RCMDVideoAdapter.ViewHolder>(datas) {

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(
        rootView
    )


    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {


        val itemRcmdVideoBinding: ItemRcmdVideoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_rcmd_video, parent, false
        )

        return ViewHolder(itemRcmdVideoBinding.root)

    }

    override fun onBindView(
        holder: ViewHolder,
        data: HomeRCMDVideoBean.DataBean.ItemBean,
        position: Int,
        size: Int,
    ) {


        val itemRcmdVideoBinding: ItemRcmdVideoBinding? =
            DataBindingUtil.getBinding(holder.itemView)


        itemRcmdVideoBinding?.apply {

            holder.itemView.setOnClickListener {
                AsVideoActivity.actionStart(context,data.bvid)
            }

            itemRcmdLikeLottie.setAnimation(R.raw.home_like)

            itemBean = data

            itemHomeRcmdLike.setOnClickListener {
                data.likeState = 1f
                itemRcmdLikeLottie.playAnimation()
                likeVideo(data.bvid, itemRcmdVideoBinding)
                notifyItemChanged(position)
            }

        }
    }

    private fun likeVideo(bvid: String, itemRcmdVideoBinding: ItemRcmdVideoBinding) {
        HttpUtils
            .addHeader("cookie", BaseApplication.cookies)
            .addParam("bvid", bvid)
            .addParam("like", "1")
            .addParam("csrf", BaseApplication.biliJct)
            .post(BilibiliApi.likeVideoPath, LikeVideoBean::class.java) {
                App.handler.post {
                    if (it.code == 0) {
                        asToast(context, "点赞成功")
                    } else {
                        itemRcmdVideoBinding.itemRcmdLikeLottie.progress = 0f
                        asToast(context, "点赞失败，${it.message}")
                    }
                }
            }
    }
}