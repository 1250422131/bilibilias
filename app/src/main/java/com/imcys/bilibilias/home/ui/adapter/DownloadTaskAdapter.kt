package com.imcys.bilibilias.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.base.utils.asLogI
import com.imcys.bilibilias.databinding.ItemDownloadTaskBinding
import javax.inject.Inject

class DownloadTaskAdapter @Inject constructor():
    ListAdapter<DownloadQueue.Task, DownloadTaskAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<DownloadQueue.Task>() {

            override fun areItemsTheSame(
                oldItem: DownloadQueue.Task,
                newItem: DownloadQueue.Task,
            ): Boolean {
                return oldItem.savePath == newItem.savePath
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: DownloadQueue.Task,
                newItem: DownloadQueue.Task,
            ): Boolean {
                return oldItem.progress == newItem.progress
            }


            override fun getChangePayload(
                oldItem: DownloadQueue.Task,
                newItem: DownloadQueue.Task,
            ): Any? {

                if (oldItem.progress != newItem.progress) {
                    newItem.payloadsType = 1
                    oldItem.payloadsType = 1
                }
                return super.getChangePayload(oldItem, newItem)
            }


        }
    ) {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = DataBindingUtil.inflate<ItemDownloadTaskBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_download_task,
            parent, false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val binding = DataBindingUtil.getBinding<ItemDownloadTaskBinding>(holder.itemView)
        binding?.apply {
            taskBean = getItem(position)
            itemDlTaskDelete.setOnClickListener {
                val dataBean = getItem(position)
                AsDialog.init(holder.itemView.context)
                    .setTitle("删除任务")
                    .setContent("注意B站视频下载链接有效时长为1小时左右，这里就只提供取消这个任务的功能了。")
                    .setPositiveButton("删除任务") {
                        dataBean.call?.cancel()
                        it.cancel()
                    }.setNegativeButton("手滑了") {
                        it.cancel()
                    }.build().show()
            }
        }
    }

}