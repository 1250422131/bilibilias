package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.model.common.BangumiFollowList
import com.imcys.bilibilias.databinding.ItemBangumiFollowBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import javax.inject.Inject

class BangumiFollowAdapter @Inject constructor() :
    ListAdapter<BangumiFollowList.DataBean.ListBean, ViewHolder>(object :
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
            return oldItem.first_ep == newItem.first_ep
        }
    }) {
    @Inject
    lateinit var networkService: NetworkService
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemBangumiFollowBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_bangumi_follow,
                parent,
                false,
            )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemBangumiFollowBinding>(holder.itemView)?.apply {
            listBean = getItem(position)
            holder.itemView.setOnClickListener {
                val cookie = BaseApplication.dataKv.decodeString(COOKIES, "").toString()

                launchIO {

                    val bangumiSeasonBean = networkService.getBangumiSeasonBeanByEpid(getItem(position).first_ep)

                    launchUI {
                        if (bangumiSeasonBean.code == 0) {
                            if (bangumiSeasonBean.result.episodes.isNotEmpty()) {
                                AsVideoActivity.actionStart(
                                    holder.itemView.context,
                                    bangumiSeasonBean.result.episodes[0].bvid,
                                )
                            } else {
                                AsVideoActivity.actionStart(
                                    holder.itemView.context,
                                    bangumiSeasonBean.result.section[0].episodes[0].bvid,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
