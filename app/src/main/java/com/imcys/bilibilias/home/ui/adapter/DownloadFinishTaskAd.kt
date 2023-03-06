package com.imcys.bilibilias.home.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.databinding.ItemDownloadTaskFinishBinding
import kotlinx.coroutines.*
import java.io.File


class DownloadFinishTaskAd : ListAdapter<DownloadFinishTaskInfo, ViewHolder>(
    object : DiffUtil.ItemCallback<DownloadFinishTaskInfo>() {
        override fun areItemsTheSame(
            oldItem: DownloadFinishTaskInfo,
            newItem: DownloadFinishTaskInfo,
        ): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(
            oldItem: DownloadFinishTaskInfo,
            newItem: DownloadFinishTaskInfo,
        ): Boolean {
            return oldItem.savePath == newItem.savePath
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            DataBindingUtil.inflate<ItemDownloadTaskFinishBinding>(LayoutInflater.from(parent.context),
                R.layout.item_download_task_finish, parent, false)

        return ViewHolder(binding.root)

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        DataBindingUtil.getBinding<ItemDownloadTaskFinishBinding>(holder.itemView)?.apply {
            downloadFinishTaskInfo = getItem(position)
            val taskId = getItem(position).id
            val task = getItem(position)

            holder.itemView.setOnClickListener {
                if (FileUtils.isFileExists(File(task.savePath))) {
                    AsDialog.init(holder.itemView.context)
                        .setTitle("文件操作")
                        .setContent("请选择下面的按钮")
                        .setPositiveButton("分享文件") {
                            val fileType: String = if (task.fileType == 0) {
                                "video/*"
                            } else {
                                "audio/*"
                            }
                            shareFile(holder.itemView.context,
                                "分享文件",
                                FileProvider.getUriForFile(holder.itemView.context,
                                    "com.imcys.bilibilias.fileProvider",
                                    File(task.savePath)),
                                fileType)
                            it.cancel()
                        }
                        .setNeutralButton("播放文件") {
                            val fileType: String = if (task.fileType == 0) {
                                "video/*"
                            } else {
                                "audio/*"
                            }
                            val intent = Intent()
                            intent.apply {
                                action = Intent.ACTION_VIEW
                                setDataAndType(FileProvider.getUriForFile(holder.itemView.context,
                                    "com.imcys.bilibilias.fileProvider",
                                    File(task.savePath)), fileType)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                holder.itemView.context.startActivity(this)
                            }

                        }
                        .setNegativeButton("取消") {
                            it.cancel()
                        }.build().show()

                } else {
                    asToast(holder.itemView.context, "该文件可能被删除\n或执行了导回B站，且删除了源文件。")
                }

            }

            itemDlFinishTaskDelete.setOnClickListener {
                bindingDeleteMethod(itemDlFinishTaskDelete, task)
            }

        }

    }

    private fun bindingDeleteMethod(
        itemDlFinishTaskDelete: ImageView,
        task: DownloadFinishTaskInfo,
    ) {

        AsDialog.init(itemDlFinishTaskDelete.context)
            .setTitle("删除警告")
            .setContent("确定删除这条纪录吗？")
            .setPositiveButton("删除纪录") {
                deleteTaskRecords(task.id)
                it.cancel()
            }.setNeutralButton("删除纪录和文件") {
                deleteTaskRecords(task.id)
                FileUtils.delete(task.savePath)
                it.cancel()
            }
            .setNegativeButton("点错了") {
                it.cancel()
            }
            .build().show()

    }

    private fun deleteTaskRecords(taskId: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val downloadFinishTaskDao =
                BaseApplication.appDatabase.downloadFinishTaskDao()
            val newTasks = withContext(Dispatchers.IO) {
                DownloadFinishTaskRepository(downloadFinishTaskDao).deleteAndReturnList(
                    downloadFinishTaskDao.findById(taskId))
            }
            //更新数据
            submitList(newTasks)
        }

    }


    private fun shareFile(context: Context, content: String?, uri: Uri?, type: String?) {

        val shareIntent = Intent(Intent.ACTION_SEND)
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = type
            //当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", content)
        } else {
            shareIntent.type = type
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        //自定义选择框的标题
        context.startActivity(Intent.createChooser(shareIntent, "分享文件"))
        //系统默认标题
    }


}