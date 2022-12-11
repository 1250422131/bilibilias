package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.databinding.ItemDownloadTaskBinding

class DownloadTaskAdapter(val tasks: MutableList<DownloadQueue.Task>) :
    RecyclerView.Adapter<DownloadTaskAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemDownloadTaskBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_download_task,
            parent,
            false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemDownloadTaskBinding>(holder.itemView)
        binding?.apply {
            taskBean = tasks[position]
        }

    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}