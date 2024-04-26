package com.imcys.bilibilias.home.ui.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.imcys.bilibilias.databinding.ItemUserVidoeDownloadBinding
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import okhttp3.internal.notifyAll


class UserVideoDownloadAdapter :
    ListAdapter<UserWorksBean.DataBean.ListBean.VlistBean, ViewHolder>(object :
        DiffUtil.ItemCallback<UserWorksBean.DataBean.ListBean.VlistBean>() {
        override fun areItemsTheSame(
            oldItem: UserWorksBean.DataBean.ListBean.VlistBean,
            newItem: UserWorksBean.DataBean.ListBean.VlistBean
        ): Boolean {
            return oldItem.bvid == newItem.bvid
        }

        override fun areContentsTheSame(
            oldItem: UserWorksBean.DataBean.ListBean.VlistBean,
            newItem: UserWorksBean.DataBean.ListBean.VlistBean
        ): Boolean {
            return oldItem.title == newItem.title
        }

    }) {

    var showState = false
    val selectList = mutableSetOf<Int>()

    var editStateChange: ((Boolean) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserVidoeDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ItemUserVidoeDownloadBinding.bind(holder.itemView)

        val data = getItem(position)
        binding.apply {
            itemUvDownloadTitle.text = data.title
            itemUvDownloadBv.text = data.bvid

            Glide.with(itemUvDownloadCover).load(data.pic).into(itemUvDownloadCover)
            if (showState) {
                itemUvDownloadCheckBox.visibility = View.VISIBLE
            } else {
                itemUvDownloadCheckBox.visibility = View.GONE
            }

            itemUvDownloadCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectList.add(position)
                } else {
                    selectList.remove(position)
                }
            }

            itemUvDownloadCheckBox.isChecked = position in selectList

            holder.itemView.setOnClickListener {
                itemUvDownloadCheckBox.isChecked = !itemUvDownloadCheckBox.isChecked
            }

        }

        holder.itemView.setOnLongClickListener {
            showState = !showState
            notifyDataSetChanged()
            editStateChange?.invoke(showState)
            selectList.clear()
            true
        }


    }

}