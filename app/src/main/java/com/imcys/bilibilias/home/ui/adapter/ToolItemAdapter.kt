package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.asLogI

import com.imcys.bilibilias.databinding.ItemToolBinding
import com.imcys.bilibilias.databinding.ItemToolVideoCardBinding
import com.imcys.bilibilias.home.ui.model.ToolItemBean

class ToolItemAdapter : ListAdapter<ToolItemBean, ViewHolder>(
    object : DiffUtil.ItemCallback<ToolItemBean>() {
        override fun areItemsTheSame(oldItem: ToolItemBean, newItem: ToolItemBean): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ToolItemBean, newItem: ToolItemBean): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = if (viewType == 0) {
            DataBindingUtil.inflate<ItemToolBinding>(LayoutInflater.from(parent.context),
                R.layout.item_tool, parent, false)
        } else {
            DataBindingUtil.inflate<ItemToolVideoCardBinding>(LayoutInflater.from(parent.context),
                R.layout.item_tool_video_card, parent, false)
        }
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItem(position).type == 0) {
            val binding = DataBindingUtil.getBinding<ItemToolBinding>(holder.itemView)
            binding?.toolItemBean = getItem(position)
            val clickEvent: () -> Unit = getItem(position).clickEvent
            holder.itemView.setOnClickListener {
                clickEvent()
            }
        } else {
            val binding = DataBindingUtil.getBinding<ItemToolVideoCardBinding>(holder.itemView)
            binding?.videoBaseBean = getItem(position).videoBaseBean
            val clickEvent: () -> Unit = getItem(position).clickEvent
            binding?.root?.setOnClickListener {
                clickEvent()
            }
        }


    }


}