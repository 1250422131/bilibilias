package com.imcys.bilibilias.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemHomeAdBinding
import com.imcys.bilibilias.databinding.ItemHomeWebAdBinding
import com.imcys.bilibilias.home.ui.model.OldHomeAdBean

class OldHomeAdAdapter : ListAdapter<OldHomeAdBean.Data, ViewHolder>(
    object : DiffUtil.ItemCallback<OldHomeAdBean.Data>() {
        override fun areItemsTheSame(
            oldItem: OldHomeAdBean.Data,
            newItem: OldHomeAdBean.Data,
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: OldHomeAdBean.Data,
            newItem: OldHomeAdBean.Data,
        ): Boolean {
            return oldItem.longTitle == newItem.longTitle
        }

    }
) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).showType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = when (viewType) {
            1 -> {
                DataBindingUtil.inflate<ItemHomeAdBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_home_ad, parent, false
                )
            }

            2 -> {
                DataBindingUtil.inflate<ItemHomeWebAdBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_home_web_ad, parent, false
                )
            }

            else -> {
                DataBindingUtil.inflate<ItemHomeAdBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_home_ad, parent, false
                )
            }
        }

        return ViewHolder(binding.root)


    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (getItem(position).showType == 1) {
            val bing = DataBindingUtil.getBinding<ItemHomeAdBinding>(holder.itemView)
            bing?.data = getItem(position)
        } else {
            DataBindingUtil.getBinding<ItemHomeWebAdBinding>(holder.itemView)?.apply {
                val webSettings: WebSettings = itemHomeWebWebView.settings
                webSettings.javaScriptEnabled = true
                webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW//解决视频无法播放问题
                itemHomeWebWebView.loadUrl(getItem(position).title)//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开

            }
        }

    }
}