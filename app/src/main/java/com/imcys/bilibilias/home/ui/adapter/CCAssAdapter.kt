package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.network.danmaku.VideoInfoV2
import com.imcys.bilibilias.databinding.ItemCcAssLangBinding

class CCAssAdapter(private val selectEvent: (index: Int) -> Unit) :
    ListAdapter<VideoInfoV2.Subtitle.MSubtitle, ViewHolder>(object :
        DiffUtil.ItemCallback<VideoInfoV2.Subtitle.MSubtitle>() {
        override fun areItemsTheSame(
            oldItem: VideoInfoV2.Subtitle.MSubtitle,
            newItem: VideoInfoV2.Subtitle.MSubtitle,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: VideoInfoV2.Subtitle.MSubtitle,
            newItem: VideoInfoV2.Subtitle.MSubtitle,
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    private var selectIndex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        DataBindingUtil.inflate<ItemCcAssLangBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cc_ass_lang,
            parent,
            false,
        ).run {
            ViewHolder(root)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        DataBindingUtil.getBinding<ItemCcAssLangBinding>(holder.itemView)?.apply {
            val position = holder.bindingAdapterPosition
            if (selectIndex == position) {
                getItem(position).check = true
            }
            subtitle = getItem(position)

            itemCcAssLy.setOnClickListener {
                selectEvent.invoke(position)

                getItem(selectIndex).check = false

                notifyItemChanged(selectIndex)

                selectIndex = position

                getItem(position).check = true

                notifyItemChanged(position)
            }
        }
    }
}
