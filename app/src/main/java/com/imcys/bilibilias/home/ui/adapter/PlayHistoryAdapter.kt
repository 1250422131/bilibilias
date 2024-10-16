package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.databinding.ItemPlayHistoryWorksBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.PlayHistoryBean
import javax.inject.Inject

class PlayHistoryAdapter @Inject constructor() :
    ListAdapter<PlayHistoryBean.DataBean.ListBean, ViewHolder>(object :
        DiffUtil.ItemCallback<PlayHistoryBean.DataBean.ListBean>() {

        override fun areItemsTheSame(
            oldItem: PlayHistoryBean.DataBean.ListBean,
            newItem: PlayHistoryBean.DataBean.ListBean,
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: PlayHistoryBean.DataBean.ListBean,
            newItem: PlayHistoryBean.DataBean.ListBean,
        ): Boolean {
            return oldItem.author_mid == newItem.author_mid
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemPlayHistoryWorksBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_play_history_works, parent, false
            )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        DataBindingUtil.getBinding<ItemPlayHistoryWorksBinding>(holder.itemView)?.apply {
            listBean = getItem(position)
            holder.itemView.setOnClickListener {
                val bvid = if (getItem(position).history.bvid != "") {
                    getItem(position).history.bvid
                } else {
                    NewVideoNumConversionUtils.av2bv(getItem(position).history.oid)
                }
                AsVideoActivity.actionStart(holder.itemView.context, bvid)
            }
        }
    }
}