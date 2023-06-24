package com.imcys.bilibilias.home.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemDedicateBinding
import com.imcys.bilibilias.home.ui.model.DedicateBean
import javax.inject.Inject

class DedicateAdapter @Inject constructor() :
    ListAdapter<DedicateBean, ViewHolder>(object : DiffUtil.ItemCallback<DedicateBean>() {
        override fun areItemsTheSame(oldItem: DedicateBean, newItem: DedicateBean): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: DedicateBean, newItem: DedicateBean): Boolean {
            return oldItem.long_title == newItem.long_title
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemDedicateBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_dedicate, parent, false
            )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemDedicateBinding>(holder.itemView)?.apply {
            dedicateBean = getItem(position)
            holder.itemView.setOnClickListener {
                if (getItem(position).link != "") {
                    val uri = Uri.parse(getItem(position).link)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }
}