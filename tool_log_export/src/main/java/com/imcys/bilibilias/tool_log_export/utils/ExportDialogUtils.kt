package com.imcys.bilibilias.tool_log_export.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.tool_log_export.R


/**
 * 导出
 */
class ExportDialogUtils {

    companion object {
        /**
         * 加载等待弹窗
         * @param context Context
         * @return BottomSheetDialog
         */
        @SuppressLint("InflateParams", "MissingInflatedId")
        fun loadDialog(context: Context, mutableState: String): BottomSheetDialog {
            //先获取View实例
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.log_export_dialog_load_bottomsheet, null, false)
            val loadText = view.findViewById<TextView>(R.id.log_export_dl_load_title)
            loadText.text = mutableState
            //设置布局背景
            //自定义方案
            //mDialogBehavior.peekHeight = 600

            return initBottomSheetDialog(context, view)
        }


        private fun initBottomSheetDialog(context: Context, view: View): BottomSheetDialog {

            val bottomSheetDialog = BottomSheetDialog(
                context,
                com.imcys.asbottomdialog.bottomdialog.R.style.asdialog_BottomSheetDialog
            )
            //设置布局
            bottomSheetDialog.setContentView(view)

            return bottomSheetDialog
        }
    }

}