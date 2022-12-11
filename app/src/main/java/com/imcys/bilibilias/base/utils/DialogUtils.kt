package com.imcys.bilibilias.base.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.icu.text.CaseMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.login.view.LoginQRModel
import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.databinding.*
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.adapter.CreateCollectionAdapter
import com.imcys.bilibilias.home.ui.adapter.VideoDefinitionAdapter
import com.imcys.bilibilias.home.ui.adapter.VideoPageAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.utils.HttpUtils
import okhttp3.internal.notifyAll
import java.net.URLEncoder


/**
 * 全局使用弹窗工具类
 */
class DialogUtils {


    companion object {

        private val TAG = DialogUtils::class.java.simpleName


        /**
         * 登录对话框
         * @param context Context
         */
        @SuppressLint("InflateParams")
        fun loginDialog(context: Context): BottomSheetDialog {
            //先获取View实例
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.dialog_login_bottomsheet, null, false)

            val bottomSheetDialog = initBottomSheetDialog(context, view)
            //用户行为val mDialogBehavior =
            initDialogBehavior(R.id.dialog_login_tip_bar, context, view)
            //自定义方案
            //mDialogBehavior.peekHeight = 600

            return bottomSheetDialog
        }


        /**
         * 本地/AS绑定 B站账号登录弹窗
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
            //传导binding过去
            binding.loginQRModel?.binding = binding
            //用户行为val mDialogBehavior =

            initDialogBehaviorBinding(binding.dialogLoginQrTipBar,
                activity,
                binding.root.parent)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            return bottomSheetDialog

        }


        // 创建一个底部弹出对话框
        fun dialog(
            context: Context,
            title: String, // 对话框标题
            message: String, // 对话框消息
            positiveButtonText: String, // 确定按钮文本
            negativeButtonText: String, // 取消按钮文本
            positiveButtonClickListener: () -> Unit, // 确定按钮点击事件处理器
            negativeButtonClickListener: () -> Unit, // 取消按钮点击事件处理器
        ): BottomSheetDialog {
            //先获取View实例
            val binding = DialogBottomsheetBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)
            bottomSheetDialog.setCancelable(false)
            binding.apply {
                // 设置标题文本
                dialogBottomSheetTitle.text = title
                // 设置消息文本
                dialogBottomSheetMessage.text = message
                // 设置确定按钮文本
                dialogBottomSheetPositiveButton.text = positiveButtonText
                // 为确定按钮添加点击事件处理器
                dialogBottomSheetPositiveButton.setOnClickListener {
                    positiveButtonClickListener()
                    bottomSheetDialog.cancel()
                }
                // 设置取消按钮文本
                dialogBottomSheetNegativeButton.text = negativeButtonText
                // 为取消按钮添加点击事件处理器
                dialogBottomSheetNegativeButton.setOnClickListener {
                    negativeButtonClickListener()
                    bottomSheetDialog.cancel()
                }

            }
            return bottomSheetDialog
        }


        /**
         * 用户信息弹窗
         * @param activity Activity
         * @param userInfoBean UserInfoBean
         * @return BottomSheetDialog
         */
        fun userDataDialog(activity: Activity, userInfoBean: UserInfoBean): BottomSheetDialog {
            //先获取View实例
            val binding = DialogUserDataBottomsheetBinding.inflate(LayoutInflater.from(activity))

            val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)

            binding.dataBean = userInfoBean.data
            //用户行为 val mDialogBehavior =

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
            //用户行为val mDialogBehavior =
            initDialogBehavior(R.id.dialog_load_tip_bar, context, view)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
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
            userCreateCollectionBean: UserCreateCollectionBean,
            selectedResult: (selectedItem: Int, selects: MutableList<Long>) -> Unit,
            finished: (selects: MutableList<Long>) -> Unit,
        ): BottomSheetDialog {

            val binding = DialogCollectionBinding.inflate(LayoutInflater.from(activity))

            val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
            //创建设置布局
            bottomSheetDialog.setContentView(binding.root)

            //val mDialogBehavior =
            initDialogBehaviorBinding(binding.dialogCollectionBar,
                activity,
                binding.root.parent)

            binding.apply {

                val collectionMutableList = mutableListOf<Long>()
                val collectionAdapter =
                    CreateCollectionAdapter(userCreateCollectionBean.data.list.toMutableList()
                    ) { position, itemBinding ->
                        //这个接口是为了处理弹窗背景问题

                        val total = collectionMutableList.size
                        //标签，判断这一次是否有重复
                        var tage = true
                        for (a in 0 until total) {

                            if (collectionMutableList[a] == userCreateCollectionBean.data.list[position].id.toLong()) {
                                tage = false
                                itemBinding.listBean?.selected = 0
                                collectionMutableList.removeAt(a)
                                break
                            }
                        }


                        if (tage) {
                            itemBinding.listBean?.selected = 1
                            collectionMutableList.add(userCreateCollectionBean.data.list[position].id.toLong())
                        }

                        binding.dialogCollectionRv.adapter?.notifyItemChanged(position)
                        selectedResult(position, collectionMutableList)


                    }


                //设置数据适配器
                dialogCollectionRv.adapter = collectionAdapter

                //设置布局加载器
                dialogCollectionRv.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                //设置完成选中收藏夹
                dialogCollectionFinishBt.setOnClickListener {

                    bottomSheetDialog.cancel()
                    finished(collectionMutableList)


                }

            }


            return bottomSheetDialog

        }


        @JvmStatic
        fun downloadVideoDialog(
            context: Context,
            videoBaseBean: VideoBaseBean,
            videoPageListData: VideoPageListData,
            dashVideoPlayBean: DashVideoPlayBean,
        ): BottomSheetDialog {
            var videoPageMutableList = mutableListOf<VideoPageListData.DataBean>()
            var selectDefinition = 80
            videoPageMutableList.add(videoPageListData.data[0])

            val binding = DialogDownloadVideoBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)


            initDialogBehaviorBinding(binding.dialogDlVideoBar, context, binding.root.parent)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            binding.apply {
                dialogDlVideoDiversityTx.setOnClickListener {
                    loadVideoPageDialog(context, videoPageListData) { it1 ->
                        videoPageMutableList.clear()
                        videoPageMutableList = it1
                        var videoPageMsg = ""
                        if (videoPageMutableList.size != 1) {
                            videoPageMutableList.forEach {
                                videoPageMsg = "$videoPageMsg${it.part} "
                            }
                            dialogDlVideoDiversityTx.text = videoPageMsg
                        }
                    }.show()


                }

                dialogDlVideoDefinitionTx.setOnClickListener {
                    loadVideoDefinition(context, dashVideoPlayBean) {
                        //这里返回的是清晰度的数值代码
                        selectDefinition = it

                        //处理下
                        dashVideoPlayBean.data.support_formats.forEach { it1 ->
                            if (it1.quality == it) dialogDlVideoDefinitionTx.text =
                                it1.new_description
                        }

                    }.show()


                }


                dialogDlVideoButton.setOnClickListener {
                    addDownloadTask(context,
                        videoBaseBean,
                        selectDefinition,
                        80,
                        videoPageMutableList)
                }


            }


            return bottomSheetDialog

        }

        private fun addDownloadTask(
            context: Context,
            videoBaseBean: VideoBaseBean,
            qn: Int,
            fnval: Int,
            videoPageMutableList: MutableList<VideoPageListData.DataBean>,
        ) {
            videoPageMutableList.forEach {
                addTask(context, it, qn, fnval, videoBaseBean, "video")
                addTask(context, it, qn, fnval, videoBaseBean, "audio")
            }
        }

        private fun addTask(
            context: Context,
            dataBean: VideoPageListData.DataBean,
            qn: Int,
            fnval: Int,
            videoBaseBean: VideoBaseBean,
            type: String,
        ) {
            Toast.makeText(context, "已添加到下载队列", Toast.LENGTH_SHORT).show()

            HttpUtils.addHeader("cookie", App.cookies)
                .addHeader("referer", "https://www.bilibili.com")
                .get("${BilibiliApi.videoPlayPath}?bvid=${videoBaseBean.data.bvid}&cid=${dataBean.cid}&qn=$qn&fnval=80&fourk=1",
                    DashVideoPlayBean::class.java) { it1 ->

                    val videoPlayData = it1.data
                    var urlIndex = 0
                    //获取视频/音频的索引
                    it1.data.dash.video.forEachIndexed { index, i ->
                        if (i.id == qn) urlIndex = index
                    }

                    val intFileType: Int
                    val fileType: String
                    val url = when (type) {
                        "video" -> {
                            intFileType = 0
                            fileType = ".mp4"
                            videoPlayData.dash.video[urlIndex].baseUrl
                        }
                        "audio" -> {
                            intFileType = 1
                            fileType = ".m4a"
                            videoPlayData.dash.audio[0].baseUrl
                        }
                        else -> throw IllegalArgumentException("Invalid type: $type")
                    }

                    App.downloadQueue.addTask(
                        url,
                        "${
                            context.getExternalFilesDir("download").toString()
                        }/${videoBaseBean.data.bvid}/cs$fileType",
                        intFileType,
                        DownloadTaskDataBean(
                            dataBean.cid.toString(),
                            dataBean.part,
                            qn.toString(),
                            dashVideoPlayBean = it1,
                            videoPageDataData = dataBean
                        )
                    ) { it2 ->
                        if (it2) {
                            Toast.makeText(context,
                                "${videoBaseBean.data.bvid}下载成功",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context,
                                "${videoBaseBean.data.bvid}下载失败",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }


        /**
         * 加载视频子集
         * @param context Context
         * @param videoPageListData VideoPageListData
         * @param finished Function1<[@kotlin.ParameterName] MutableList<Long>, Unit>
         * @return BottomSheetDialog
         */
        private fun loadVideoPageDialog(
            context: Context,
            videoPageListData: VideoPageListData,
            finished: (selects: MutableList<VideoPageListData.DataBean>) -> Unit,
        ): BottomSheetDialog {

            val binding = DialogCollectionBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //创建设置布局
            bottomSheetDialog.setContentView(binding.root)

            //val mDialogBehavior =
            initDialogBehaviorBinding(binding.dialogCollectionBar,
                context,
                binding.root.parent)


            binding.apply {
                dialogCollectionTitle.text = "请选择视频子集"

                val videoPageMutableList = mutableListOf<VideoPageListData.DataBean>()
                dialogCollectionRv.adapter =
                    VideoPageAdapter(videoPageListData.data) { position, itemBinding ->
                        //这个接口是为了处理弹窗背景问题

                        val total = videoPageMutableList.size
                        //标签，判断这一次是否有重复
                        var tage = true
                        for (a in 0 until total) {

                            if (videoPageMutableList[a].cid.toLong() == videoPageListData.data[position].cid.toLong()) {
                                tage = false
                                itemBinding.dataBean?.selected = 0
                                videoPageMutableList.removeAt(a)
                                break
                            }

                        }


                        if (tage) {
                            itemBinding.dataBean?.selected = 1
                            videoPageMutableList.add(videoPageListData.data[position])
                        }

                        dialogCollectionRv.adapter?.notifyItemChanged(position)

                    }


                //设置布局加载器
                dialogCollectionRv.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                //设置完成选中的子集
                dialogCollectionFinishBt.setOnClickListener {

                    bottomSheetDialog.cancel()
                    finished(videoPageMutableList)
                }
            }

            return bottomSheetDialog


        }


        /**
         * 加载视频清晰度
         * @param context Context
         * @param videoPlayBean VideoPageListData
         * @param finished Function1<[@kotlin.ParameterName] Int, Unit>
         */
        private fun loadVideoDefinition(
            context: Context,
            dashVideoPlayBean: DashVideoPlayBean,
            finished: (selects: Int) -> Unit,
        ): BottomSheetDialog {

            var selectDefinition = 80
            val binding = DialogCollectionBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //创建设置布局
            bottomSheetDialog.setContentView(binding.root)

            //val mDialogBehavior =
            initDialogBehaviorBinding(binding.dialogCollectionBar,
                context,
                binding.root.parent)


            binding.apply {
                dialogCollectionTitle.text = "请选择缓存清晰度"

                dialogCollectionRv.adapter =
                    VideoDefinitionAdapter(dashVideoPlayBean.data.accept_description) { position, _ ->
                        selectDefinition = dashVideoPlayBean.data.accept_quality[position]

                    }


                //设置布局加载器
                dialogCollectionRv.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                //设置完成选中收藏夹
                dialogCollectionFinishBt.setOnClickListener {
                    bottomSheetDialog.cancel()
                    finished(selectDefinition)
                }
            }

            return bottomSheetDialog

        }


        //TODO 常用方法封装
        @JvmStatic
        private fun initDialogBehaviorBinding(
            tipView: View,
            context: Context,
            viewGroup: ViewParent,
        ) {

            //用户行为
            val mDialogBehavior = BottomSheetBehavior.from(viewGroup as View)
            mDialogBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                @SuppressLint("UseCompatLoadingForDrawables", "SwitchIntDef")
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
            //用户行为
            val mDialogBehavior = BottomSheetBehavior.from(view.parent as View)
            mDialogBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                @SuppressLint("UseCompatLoadingForDrawables", "SwitchIntDef")
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

        /**
         * 设置弹窗布局
         * @param context Context
         * @param view View
         * @return BottomSheetDialog
         */
        @JvmStatic
        private fun initBottomSheetDialog(context: Context, view: View): BottomSheetDialog {

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(view)

            return bottomSheetDialog
        }


    }
}