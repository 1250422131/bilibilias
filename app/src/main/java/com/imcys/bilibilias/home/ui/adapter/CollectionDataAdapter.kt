package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.utils.VideoUtils
import com.imcys.bilibilias.databinding.ItemCollectionWorksBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.common.base.model.Collections
import javax.inject.Inject

class CollectionDataAdapter @Inject constructor() :
    ListAdapter<Collections.Media, ViewHolder>(object :
        DiffUtil.ItemCallback<Collections.Media>() {
        override fun areItemsTheSame(
            oldItem: Collections.Media,
            newItem: Collections.Media,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Collections.Media,
            newItem: Collections.Media,
        ): Boolean {
            return oldItem.bvid == newItem.bvid
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemCollectionWorksBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_collection_works,
                parent,
                false,
            )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding =
            DataBindingUtil.getBinding<ItemCollectionWorksBinding>(holder.itemView)?.apply {
                holder.itemView.setOnClickListener {
                    val bvid = if (getItem(position).bvid != "") {
                        getItem(position).bvid
                    } else {
                        VideoUtils.toBvidOffline(getItem(position).id.toLong())
                    }
                    AsVideoActivity.actionStart(holder.itemView.context, bvid)
                }
            }
    }
}
