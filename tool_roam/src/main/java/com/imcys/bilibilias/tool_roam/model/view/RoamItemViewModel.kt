package com.imcys.bilibilias.tool_roam.model.view

import android.content.Context
import android.view.View
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.entity.RoamInfo
import com.imcys.bilibilias.tool_roam.databinding.ActivityRoamMainBinding
import kotlinx.coroutines.*

class RoamItemViewModel(val binding: ActivityRoamMainBinding) {


    fun deleteItem(view: View, roamInfo: RoamInfo) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        AsDialog.init(view.context)
            .setTitle("删除警告")
            .setContent("确定要删除这条纪录吗？")
            .setPositiveButton("确定") {
                //删除
                coroutineScope.launch(Dispatchers.IO) {
                    deleteRoam(view.context, roamInfo)
                }
                it.cancel()

            }
            .setNegativeButton("手滑了") {
                it.cancel()
            }
    }

    /**
     * 删除纪录
     * @param roamInfo RoamInfo
     */
    private suspend fun deleteRoam(context: Context, roamInfo: RoamInfo) {
        val roamDao = AppDatabase.getDatabase(context).roamDao().apply {
            delete(roamInfo)
        }
    }


}