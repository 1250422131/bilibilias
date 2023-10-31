package com.imcys.bilibilias.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.*
import com.imcys.bilibilias.home.ui.adapter.*
import com.imcys.bilibilias.home.ui.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 全局使用弹窗工具类
 */
object DialogUtils {
    /**
     * 登录对话框
     * @param context Context
     */
    @SuppressLint("InflateParams")
    fun loginDialog(context: Context): BottomSheetDialog {
        val binding = DialogLoginBottomsheetBinding.inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setCancelable(false)

        // 用户行为val mDialogBehavior =
        initDialogBehaviorBinding(
            binding.dialogLoginTipBar,
            context,
            binding.root.parent,
        )
        // 自定义方案
        // mDialogBehavior.peekHeight = 600

        return bottomSheetDialog
    }


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
        // 先获取View实例
        val binding = DialogBottomsheetBinding.inflate(LayoutInflater.from(context))

        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        // 设置布局
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setCancelable(cancelable)

        initDialogBehaviorBinding(binding.dialogBottomSheetBar, context, binding.root.parent)

        binding.apply {
            // 设置标题文本
            dialogBottomSheetTitle.text = title
            // 设置消息文本
            dialogBottomSheetMessage.text = message
            // 设置确定按钮文本
            dialogBottomSheetPositiveButton.text = positiveButtonText
            // 为确定按钮添加点击事件处理器
            positiveButtonClickListener?.apply {
                dialogBottomSheetPositiveButton.visibility = View.VISIBLE
                dialogBottomSheetPositiveButton.setOnClickListener {
                    positiveButtonClickListener()
                    bottomSheetDialog.cancel()
                }
            }
            // 设置取消按钮文本
            dialogBottomSheetNegativeButton.text = negativeButtonText
            // 为取消按钮添加点击事件处理器
            negativeButtonClickListener?.apply {
                dialogBottomSheetNegativeButton.visibility = View.VISIBLE
                dialogBottomSheetNegativeButton.setOnClickListener {
                    negativeButtonClickListener()
                    bottomSheetDialog.cancel()
                }
            }

            imageUrl?.apply {
                dialogBottomSheetImage.visibility = View.VISIBLE
                Glide.with(context).load(imageUrl).into(dialogBottomSheetImage)
            }
        }
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
     * 加载用户收藏文件夹
     * @param activity Activity
     * @param userCreateCollectionBean UserCreateCollectionBean
     * @param selectedResult Function2<[@kotlin.ParameterName] Int, [@kotlin.ParameterName] MutableList<Long>, Unit>
     * @return BottomSheetDialog
     */
    @SuppressLint("NotifyDataSetChanged")
    fun loadUserCreateCollectionDialog(
        activity: Activity,
    ): BottomSheetDialog {
        val binding = DialogCollectionBinding.inflate(LayoutInflater.from(activity))

        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
        // 创建设置布局
        bottomSheetDialog.setContentView(binding.root)

        // val mDialogBehavior =
        initDialogBehaviorBinding(
            binding.dialogCollectionBar,
            activity,
            binding.root.parent,
        )

        binding.apply {
            val collectionMutableList = mutableListOf<Long>()
            // 这个接口是为了处理弹窗背景问题

            val total = collectionMutableList.size
            // 标签，判断这一次是否有重复
        }

        // 设置完成选中收藏夹
        // dialogCollectionFinishBt.setOnClickListener {
        //     bottomSheetDialog.cancel()
        //     finished(collectionMutableList)
        // }
        return bottomSheetDialog
    }


    @JvmStatic
    internal fun initDialogBehaviorBinding(
        tipView: View,
        context: Context,
        viewGroup: ViewParent,
    ) {
        // 用户行为
        val mDialogBehavior = BottomSheetBehavior.from(viewGroup as View)
        mDialogBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("UseCompatLoadingForDrawables", "SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 拖动监听
                val linearLayout: View = tipView
                when (newState) {
                    1 -> {
                        linearLayout.background = context.getDrawable(R.color.color_primary)
                    }

                    4 -> {
                        linearLayout.background =
                            context.getDrawable(R.color.color_primary_variant)
                    }

                    3 -> {
                        linearLayout.background =
                            context.getDrawable(R.color.color_primary_variant)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 状态改变监听
            }
        })
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

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
