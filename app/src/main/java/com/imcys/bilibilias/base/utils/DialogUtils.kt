package com.imcys.bilibilias.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.*
import com.imcys.bilibilias.home.ui.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 全局使用弹窗工具类
 */
object DialogUtils {


    /**
     * 构建底部对话框
     * @param context Context
     * @param title String
     * @param message String
     * @param positiveButtonText String
     * @param negativeButtonText String
     * @param cancelable Boolean
     * @param positiveButtonClickListener Function0<Unit>?
     * @param negativeButtonClickListener Function0<Unit>?
     * @return BottomSheetDialog
     */
    fun dialog(
        context: Context,
        title: String, // 对话框标题
        message: String, // 对话框消息
        positiveButtonText: String = "", // 确定按钮文本
        negativeButtonText: String? = "", // 取消按钮文本
        cancelable: Boolean = true,
        imageUrl: String? = null,
        positiveButtonClickListener: (() -> Unit)? = null, // 确定按钮点击事件处理器
        negativeButtonClickListener: (() -> Unit)? = null, // 取消按钮点击事件处理器
    ): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        return bottomSheetDialog
    }

    /**
     * 加载等待弹窗
     * @param context Context
     * @return BottomSheetDialog
     */
    @SuppressLint("InflateParams")
    fun loadDialog(context: Context): BottomSheetDialog {
        // 先获取View实例
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.dialog_load_bottomsheet, null, false)
        // 设置布局背景
        val bottomSheetDialog = initBottomSheetDialog(context, view)
        // 用户行为val mDialogBehavior =
        initDialogBehavior(R.id.dialog_load_tip_bar, context, view)
        // 自定义方案
        // mDialogBehavior.peekHeight = 600
        return bottomSheetDialog
    }


    /**
     * 设置拖动监听
     * @param barId Int
     * @param context Context
     * @param view View
     * @return BottomSheetBehavior<View>
     */

    @JvmStatic
    fun initDialogBehavior(
        barId: Int,
        context: Context,
        view: View,
    ): BottomSheetBehavior<View> {
        // 用户行为
        val mDialogBehavior = BottomSheetBehavior.from(view.parent as View)
        mDialogBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("UseCompatLoadingForDrawables", "SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 拖动监听
                val linearLayout: View? = view.findViewById(barId)
                when (newState) {
                    1 -> {
                        linearLayout?.background = context.getDrawable(R.color.color_primary)
                    }

                    4 -> {
                        linearLayout?.background =
                            context.getDrawable(R.color.color_primary_variant)
                    }

                    3 -> {
                        linearLayout?.background =
                            context.getDrawable(R.color.color_primary_variant)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 状态改变监听
            }
        })

        return mDialogBehavior
    }

    /**
     * 设置弹窗布局
     * @param context Context
     * @param view View
     * @return BottomSheetDialog
     */
    @JvmStatic
    private fun initBottomSheetDialog(context: Context, view: View): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        // 设置布局
        bottomSheetDialog.setContentView(view)

        return bottomSheetDialog
    }
}
