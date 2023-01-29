package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.databinding.ItemBangumiFollowBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.BangumiFollowList
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.utils.http.HttpUtils

class BangumiFollowAdapter : ListAdapter<BangumiFollowList.DataBean.ListBean, ViewHolder>(object :
    DiffUtil.ItemCallback<BangumiFollowList.DataBean.ListBean>() {
    override fun areItemsTheSame(
        oldItem: BangumiFollowList.DataBean.ListBean,
        newItem: BangumiFollowList.DataBean.ListBean,
    ): Boolean {
        return oldItem.media_id == newItem.media_id
    }

    override fun areContentsTheSame(
        oldItem: BangumiFollowList.DataBean.ListBean,
        newItem: BangumiFollowList.DataBean.ListBean,
    ): Boolean {
        return oldItem.first_ep == oldItem.first_ep
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            DataBindingUtil.inflate<ItemBangumiFollowBinding>(LayoutInflater.from(parent.context),
                R.layout.item_bangumi_follow, parent, false)

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemBangumiFollowBinding>(holder.itemView)?.apply {
            listBean = getItem(position)
            holder.itemView.setOnClickListener {

                HttpUtils.addHeader("cookie", App.cookies)
                    .get("${BilibiliApi.bangumiVideoDataPath}?ep_id=${getItem(position).first_ep}",
                        BangumiSeasonBean::class.java) {
                        if (it.code == 0){
                            AsVideoActivity.actionStart(holder.itemView.context,it.result.episodes[0].bvid)
                        }
                    }
            }
        }

    }
}