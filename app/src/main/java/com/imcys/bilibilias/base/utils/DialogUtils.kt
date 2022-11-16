package com.imcys.bilibilias.base.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.login.view.LoginQRModel
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.databinding.DialogCollectionBinding
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import com.imcys.bilibilias.databinding.DialogUserDataBottomsheetBinding
import com.imcys.bilibilias.databinding.ItemCollectionBinding
import com.imcys.bilibilias.home.ui.adapter.CreateCollectionAdapter
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import java.net.URLEncoder


/**
 * 全局使用弹窗工具类
 */
class DialogUtils {


    companion object {

        private val TAG = DialogUtils::class.java.simpleName


        /**
         * 登陆对话框
         * @param context Context
         */
        @SuppressLint("InflateParams")
        fun loginDialog(context: Context): BottomSheetDialog {
            //先获取View实例
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.dialog_login_bottomsheet, null, false)

            val bottomSheetDialog = initBottomSheetDialog(context, view)
            //用户行为
            val mDialogBehavior = initDialogBehavior(R.id.dialog_login_tip_bar, context, view)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            return bottomSheetDialog
        }


        /**
         * 本地/AS绑定 B站账号登陆弹窗
         * @param activity Activity
         * @param loginQrcodeBean LoginQrcodeBean
         * @return BottomSheetDialog
         */
        fun loginQRDialog(
            activity: Activity,
            loginQrcodeBean: LoginQrcodeBean,
            responseResult: (Int, LoginStateBean) -> Unit,
        ): BottomSheetDialog {

            val binding: DialogLoginQrBottomsheetBinding =
                DialogLoginQrBottomsheetBinding.inflate(LayoutInflater.from(activity))


            val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)
            binding.dataBean = loginQrcodeBean.data
            binding.loginQRModel = LoginQRModel()
            binding.loginQRModel?.responseResult = responseResult
            binding.loginQRModel?.activity = activity
            //传导binding过去
            binding.loginQRModel?.binding = binding
            //用户行为
            val mDialogBehavior =
                initDialogBehaviorBinding(binding.dialogLoginQrTipBar,
                    activity,
                    binding.root.parent)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            return bottomSheetDialog

        }

        fun userDataDialog(activity: Activity, userInfoBean: UserInfoBean): BottomSheetDialog {
            //先获取View实例
            val binding = DialogUserDataBottomsheetBinding.inflate(LayoutInflater.from(activity))

            val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)

            binding.dataBean = userInfoBean.data
            //用户行为
            val mDialogBehavior =
                initDialogBehaviorBinding(binding.dialogUserDataBar, activity, binding.root.parent)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            return bottomSheetDialog

        }


        @SuppressLint("InflateParams")
        fun loadDialog(context: Context): BottomSheetDialog {
            //先获取View实例
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.dialog_load_bottomsheet, null, false)
            //设置布局背景
            val bottomSheetDialog = initBottomSheetDialog(context, view)
            //用户行为
            val mDialogBehavior = initDialogBehavior(R.id.dialog_load_tip_bar, context, view)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            return bottomSheetDialog
        }


        fun loadUserCreateCollectionDialog(
            activity: Activity,
            userCreateCollectionBean: UserCreateCollectionBean,
            selectedResult: (selectedItem: Int, binding: ItemCollectionBinding) -> Unit,
        ): BottomSheetDialog {

            val binding = DialogCollectionBinding.inflate(LayoutInflater.from(activity))

            val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
            //创建设置布局
            bottomSheetDialog.setContentView(binding.root)

            val mDialogBehavior =
                initDialogBehaviorBinding(binding.dialogCollectionBar,
                    activity,
                    binding.root.parent)

            binding.apply {
                val collectionAdapter =
                    CreateCollectionAdapter(userCreateCollectionBean.data.list.toMutableList())

                //设置回调接受参数
                collectionAdapter.selectedResult = selectedResult

                //设置数据适配器
                dialogCollectionRv.adapter = collectionAdapter

                //设置布局加载器
                dialogCollectionRv.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }


            return bottomSheetDialog

        }


        //TODO 常用方法封装

        private fun initDialogBehaviorBinding(
            tipView: View,
            context: Context,
            viewGroup: ViewParent,
        ) {
            //用户行为
            val mDialogBehavior = BottomSheetBehavior.from(viewGroup as View)
            mDialogBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    //拖动监听
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
                    //状态改变监听
                }

            })
        }

        private fun initDialogBehavior(
            barId: Int,
            context: Context,
            view: View,
        ): BottomSheetBehavior<View> {
            //用户行为

            val mDialogBehavior = BottomSheetBehavior.from(view.parent as View)
            mDialogBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    //拖动监听
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
                    //状态改变监听


                }

            })

            return mDialogBehavior
        }

        private fun initBottomSheetDialog(context: Context, view: View): BottomSheetDialog {

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(view)

            return bottomSheetDialog
        }


    }
}