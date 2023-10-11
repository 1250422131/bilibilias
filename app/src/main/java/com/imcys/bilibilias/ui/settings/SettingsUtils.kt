package com.imcys.bilibilias.ui.settings

import android.app.Activity
import android.content.Context
import me.rosuh.filepicker.bean.FileItemBeanImpl
import me.rosuh.filepicker.config.AbstractFileFilter
import me.rosuh.filepicker.config.FilePickerManager

fun requestFileSavePath(context: Context) {
    FilePickerManager
        .from(context as Activity)
        .maxSelectable(1)
        .filter(
            object : AbstractFileFilter() {
                override fun doFilter(listData: ArrayList<FileItemBeanImpl>): ArrayList<FileItemBeanImpl> {
                    return ArrayList(listData.filter { it.isDir })
                }
            }
        )
        .skipDirWhenSelect(false)
        .forResult(FilePickerManager.REQUEST_CODE)
}
