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
import com.imcys.bilibilias.base.model.task.DownloadTaskInfo
import com.imcys.bilibilias.base.utils.STATE_DOWNLOADING
import com.imcys.bilibilias.base.utils.STATE_DOWNLOAD_END
import com.imcys.bilibilias.base.utils.STATE_DOWNLOAD_ERROR
import com.imcys.bilibilias.base.utils.STATE_DOWNLOAD_WAIT
import com.imcys.bilibilias.base.utils.STATE_MERGE
import com.imcys.bilibilias.databinding.ItemDownloadTaskBinding
import javax.inject.Inject

class DownloadTaskAdapter @Inject constructor() :
    ListAdapter<DownloadTaskInfo, DownloadTaskAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<DownloadTaskInfo>() {

            override fun areItemsTheSame(
                oldItem: DownloadTaskInfo,
                newItem: DownloadTaskInfo,
            ): Boolean {
                return oldItem.savePath == newItem.savePath
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: DownloadTaskInfo,
                newItem: DownloadTaskInfo,
            ): Boolean {
                return oldItem.progress == newItem.progress
            }

        },
    ) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemDownloadTaskBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_download_task,
            parent,
            false,
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemDownloadTaskBinding>(holder.itemView)
        binding?.apply {
            taskBean = getItem(position)

            val state = when (taskBean?.state ?: -100) {
                STATE_DOWNLOAD_END -> "下载完成"
                STATE_DOWNLOAD_WAIT -> "等待下载"
                STATE_DOWNLOADING -> "正在下载"
                STATE_MERGE -> "正在合并"
                STATE_DOWNLOAD_ERROR -> "下载出错"
                else -> "未知"
            }

            itemDownloadTaskState.text = state

            itemDlTaskDelete.setOnClickListener {
                val dataBean = getItem(position)

                AsDialog.init(holder.itemView.context).build {
                    config = {
                        title = "删除任务"
                        content =
                            "注意B站视频下载链接有效时长为1小时左右，这里就只提供取消这个任务的功能了。"
                        positiveButtonText = "删除任务"
                        positiveButton = {
                            dataBean.call?.cancel()
                            it.cancel()
                        }
                        negativeButtonText = "手滑了"
                        negativeButton = {
                            it.cancel()
                        }
                    }
                }.show()
            }
        }
    }
}
