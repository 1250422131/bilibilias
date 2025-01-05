package com.imcys.bilibilias.home.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.databinding.ItemDownloadTaskFinishBinding
import com.liulishuo.okdownload.OkDownloadProvider
import com.liulishuo.okdownload.OkDownloadProvider.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


class DownloadFinishTaskAd @Inject constructor() : ListAdapter<DownloadFinishTaskInfo, ViewHolder>(

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
            val showEditEqual = oldItem.showEdit == newItem.showEdit
            val selectStateEqual = oldItem.selectState == newItem.selectState

            return showEditEqual && selectStateEqual
        }
    },
) {

    @Inject
    lateinit var downloadFinishTaskRepository: DownloadFinishTaskRepository
    var mLongClickEvent: () -> Boolean = { false }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemDownloadTaskFinishBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_download_task_finish,
                parent,
                false,
            )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        DataBindingUtil.getBinding<ItemDownloadTaskFinishBinding>(holder.itemView)?.apply {
            downloadFinishTaskInfo = getItem(position)
            val taskId = getItem(position).id
            val task = getItem(position)

            holder.itemView.setOnLongClickListener {
                if (mLongClickEvent()) {
                    val newList = currentList.map { it.copy(showEdit = true) }
                    submitList(newList)
                } else {
                    val newList = currentList.map { it.copy(showEdit = false) }
                    submitList(newList)
                }
                true
            }

            if (task.showEdit) {
                itemDlFinishTaskDelete.visibility = View.GONE
                itemDlFinishTaskEditCheckBox.visibility = View.VISIBLE
            } else {
                itemDlFinishTaskEditCheckBox.visibility = View.GONE
                itemDlFinishTaskDelete.visibility = View.VISIBLE
            }

            itemDlFinishTaskEditCheckBox.isChecked = task.selectState

            // 点击
            holder.itemView.setOnClickListener {
                if (task.showEdit) {
                    task.selectState = !task.selectState
                    itemDlFinishTaskEditCheckBox.isChecked = task.selectState
                    return@setOnClickListener
                }
                val safExist = task.safPath.isNotEmpty() && DocumentFile.fromSingleUri(
                    context,
                    Uri.parse(task.safPath)
                )?.exists() == true

                if (FileUtils.isFileExists(File(task.savePath)) || safExist) {
                    AsDialog.init(holder.itemView.context)
                        .setTitle("文件操作")
                        .setContent("请选择下面的按钮")
                        .setPositiveButton("分享文件") {
                            val fileType: String = if (task.fileType == 0) {
                                "video/*"
                            } else {
                                "audio/*"
                            }
                            shareFile(
                                holder.itemView.context,
                                "分享文件",
                                FileProvider.getUriForFile(
                                    holder.itemView.context,
                                    "com.imcys.bilibilias.fileProvider",
                                    File(task.savePath),
                                ),
                                fileType,
                            )
                            it.cancel()
                        }
                        .setNeutralButton("播放文件") {
                            val fileType: String = if (task.fileType == 0) {
                                "video/*"
                            } else {
                                "audio/*"
                            }
                            val fileUri = FileProvider.getUriForFile(
                                holder.itemView.context,
                                "com.imcys.bilibilias.fileProvider",
                                File(task.savePath),
                            )

                            val intent = Intent()
                            intent.apply {
                                action = Intent.ACTION_VIEW
                                setDataAndType(fileUri, fileType,)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                holder.itemView.context.startActivity(this)
                            }

                        }
                        .setNegativeButton("取消") {
                            it.cancel()
                        }.build().show()
                } else {
                    asToast(
                        holder.itemView.context,
                        "该文件可能被删除\n或执行了导回B站，且删除了源文件。",
                    )
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
            .setContent("确定删除这条记录吗？")
            .setPositiveButton("删除记录") {
                deleteTaskRecords(task.id)
                it.cancel()
            }.setNeutralButton("删除记录和文件") {
                deleteTaskRecords(task.id)
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context)

                val saveUriPath = sharedPreferences.getString(
                    "user_download_save_uri_path",
                    null,
                )
                if (saveUriPath != null) {
                    // 走SAF
                    var dlFileDocument = DocumentFile.fromTreeUri(
                        context,
                        Uri.parse(saveUriPath)
                    )
                    launchIO {
                        // 无需等待
                        val mPath = task.savePath.replace("/storage/emulated/0/", "")
                        val docList = mPath.split("/")
                        docList.forEachIndexed { index, name ->
                            dlFileDocument = dlFileDocument?.findFile(name) ?: dlFileDocument
                            if (index == docList.size - 1) {
                                if (dlFileDocument?.isFile == true && dlFileDocument?.isDirectory != true && dlFileDocument?.name == docList.last()) {
                                    dlFileDocument?.delete()
                                } else {
                                    launchUI {
                                        asToast(
                                            context,
                                            task.videoTitle + "删除失败，请自行手动删除，这可能与修改存储路径有关系。"
                                        )
                                    }
                                }

                            }
                        }
                    }
                } else {
                    FileUtils.delete(task.savePath)
                }
                it.cancel()
            }
            .setNegativeButton("点错了") {
                it.cancel()
            }
            .build().show()
    }

    private fun deleteTaskRecords(taskId: Int) {
        launchIO {
            val newTasks = withContext(Dispatchers.Default) {
                downloadFinishTaskRepository.deleteAndReturnList(
                    downloadFinishTaskRepository.findById(taskId),
                )
            }
            // 更新数据
            submitList(newTasks)
        }
    }

    private fun shareFile(context: Context, content: String?, uri: Uri?, type: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = type
            // 当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", content)
        } else {
            shareIntent.type = type
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        // 自定义选择框的标题
        context.startActivity(Intent.createChooser(shareIntent, "分享文件"))
        // 系统默认标题
    }
}
