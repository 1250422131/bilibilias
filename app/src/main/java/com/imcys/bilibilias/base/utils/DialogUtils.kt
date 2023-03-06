package com.imcys.bilibilias.base.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mobstat.StatService
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.login.view.LoginQRModel
import com.imcys.bilibilias.base.model.login.view.LoginViewModel
import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.extend.toAsDownloadSavePath
import com.imcys.bilibilias.common.base.utils.AsVideoNumUtils
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.*
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.adapter.*
import com.imcys.bilibilias.home.ui.model.*
import com.microsoft.appcenter.analytics.Analytics
import kotlinx.coroutines.flow.flow


/**
 * 全局使用弹窗工具类
 */
class DialogUtils {


    companion object {

        private val TAG = DialogUtils::class.java.simpleName
        const val DASH_TYPE = 1
        const val MP4_TYPE = 2
        const val ADM_DOWNLOAD = 3
        const val IDM_DOWNLOAD = 2
        const val APP_DOWNLOAD = 1

        const val VIDEOANDAUDIO = 1
        const val ONLY_AUDIO = 2
        const val ONLY_VIDEO = 3


        /**
         * 登录对话框
         * @param context Context
         */
        @SuppressLint("InflateParams")
        fun loginDialog(context: Context): BottomSheetDialog {


            val binding = DialogLoginBottomsheetBinding.inflate(LayoutInflater.from(context))
            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            binding.loginViewModel = LoginViewModel()
            binding.loginViewModel?.context = context
            binding.apply {
                dialogLoginBiliQr.setOnClickListener {
                    (context as HomeActivity).homeFragment.loadLogin()
                    bottomSheetDialog.cancel()
                }
            }
            bottomSheetDialog.setContentView(binding.root)
            bottomSheetDialog.setCancelable(false)


            //用户行为val mDialogBehavior =
            initDialogBehaviorBinding(
                binding.dialogLoginTipBar,
                context,
                binding.root.parent
            )
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
            bottomSheetDialog.setCancelable(false)
            binding.dataBean = loginQrcodeBean.data
            binding.loginQRModel = LoginQRModel()
            binding.loginQRModel?.responseResult = responseResult
            //传导binding过去
            binding.loginQRModel?.binding = binding
            //用户行为val mDialogBehavior =

            initDialogBehaviorBinding(
                binding.dialogLoginQrTipBar,
                activity,
                binding.root.parent
            )
            //自定义方案
            //mDialogBehavior.peekHeight = 600
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
            //先获取View实例
            val binding = DialogBottomsheetBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
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

            binding.dataBean = userInfoBean
            //用户行为 val mDialogBehavior =
            binding.dialogUserDataFinishBt.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            initDialogBehaviorBinding(binding.dialogUserDataBar, activity, binding.root.parent)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            return bottomSheetDialog

        }


        /**
         * 加载等待弹窗
         * @param context Context
         * @return BottomSheetDialog
         */
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
         * 加载视频下载类型选择器弹窗
         * @param context Context
         * @param finished Function2<[@kotlin.ParameterName] Int, [@kotlin.ParameterName] String, Unit>
         * @return BottomSheetDialog
         */
        @SuppressLint("CommitPrefEdits")
        private fun loadDownloadTypeDialog(
            context: Context,
            finished: (selects: Int, typeName: String) -> Unit,
        ): BottomSheetDialog {
            val binding = DialogDownloadTypeBinding.inflate(LayoutInflater.from(context))
            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //创建设置布局
            bottomSheetDialog.setContentView(binding.root)
            //val mDialogBehavior =
            initDialogBehaviorBinding(
                binding.dialogDlTypeTopBar,
                context,
                binding.root.parent
            )
            binding.apply {

                dialogDlTypeDashBt.setOnClickListener {

                    PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("user_download_type", 1).apply()
                    finished(1, "Dash")
                    bottomSheetDialog.cancel()
                }

                dialogDlTypeFlvBt.setOnClickListener {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("user_download_type", 2).apply()
                    finished(2, "MP4")
                    bottomSheetDialog.cancel()
                }
            }

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
            initDialogBehaviorBinding(
                binding.dialogCollectionBar,
                activity,
                binding.root.parent
            )

            binding.apply {

                val collectionMutableList = mutableListOf<Long>()
                val collectionAdapter =
                    CreateCollectionAdapter(
                        userCreateCollectionBean.data.list.toMutableList()
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


        private fun addThirdPartyData(
            bvid: String,
            aid: Int,
            mid: Long,
            name: String,
            copyright: Int,
            tName: String,
            downloadTool: Int,
            downloadType: Int,
            downloadCondition: Int,
            toneQuality: Int,
        ) {

            val mDownloadTool = when (downloadTool) {
                IDM_DOWNLOAD -> {
                    "IDM"
                }
                APP_DOWNLOAD -> {
                    "APP"
                }
                ADM_DOWNLOAD -> {
                    "ADM"
                }
                else -> {
                    TODO("意外出现")
                }
            }

            val mDownloadType = when (downloadType) {
                DASH_TYPE -> {
                    "DASH"
                }
                MP4_TYPE -> {
                    "MP4"
                }
                else -> {
                    TODO("意外出现")
                }
            }

            //构建微软事件提交
            var properties = mutableMapOf<String, String>()
            properties["bvid"] = bvid
            properties["upName"] = name
            properties["copyright"] = copyright.toString()
            properties["tName"] = tName
            properties["downloadTool"] = mDownloadTool
            properties["downloadType"] = mDownloadType
            properties["downloadCondition"] = downloadCondition.toString()
            properties["toneQuality"] = toneQuality.toString()
            Analytics.trackEvent("AnalysisVideo", properties)
            //构建百度事件提交

            properties = mutableMapOf()
            properties["tName"] = tName
            properties["copyright"] = copyright.toString()
            properties["downloadTool"] = mDownloadTool
            properties["downloadType"] = mDownloadType
            StatService.onEvent(
                App.context,
                "AnalysisVideo",
                "解析视频",
                1,
                properties
            )


        }


        /**
         * 判断视频下载方案
         * @param context Context
         * @param downloadType Int
         * @param downloadTool Int
         * @param videoBaseBean VideoBaseBean
         * @param qn Int
         * @param fnval Int
         * @param videoPageMutableList MutableList<DataBean>
         */
        private fun downloadTaskStream(
            context: Context,
            downloadType: Int,
            downloadTool: Int,
            downloadCondition: Int,
            toneQuality: Int,
            videoBaseBean: VideoBaseBean,
            qn: Int,
            fnval: Int,
            videoPageMutableList: MutableList<VideoPageListData.DataBean>,
        ) {

            //向第三方统计提交数据
            addThirdPartyData(
                videoBaseBean.data.bvid,
                videoBaseBean.data.aid,
                videoBaseBean.data.owner.mid,
                videoBaseBean.data.owner.name,
                videoBaseBean.data.copyright,
                videoBaseBean.data.tname,
                downloadTool,
                downloadType,
                downloadCondition,
                toneQuality
            )

            //首先判断是什么类型
            when (downloadType) {
                DASH_TYPE -> {
                    addDownloadTask(
                        context,
                        videoBaseBean,
                        qn,
                        80,
                        downloadTool,
                        downloadCondition,
                        toneQuality,
                        videoPageMutableList
                    )
                }
                MP4_TYPE -> {
                    addFlvDownloadTask(
                        context,
                        videoBaseBean,
                        qn,
                        80,
                        downloadTool,
                        videoPageMutableList
                    )

                }
            }
        }


        /**
         * 判断番剧下载方案
         * @param context Context
         * @param downloadType Int
         * @param downloadTool Int
         * @param videoBaseBean VideoBaseBean
         * @param qn Int
         * @param fnval Int
         * @param videoPageMutableList MutableList<DataBean>
         */
        @JvmName("downloadTaskStream1")
        private fun downloadTaskStream(
            context: Context,
            downloadType: Int,
            downloadTool: Int,
            downloadCondition: Int,
            toneQuality: Int,
            videoBaseBean: VideoBaseBean,
            qn: Int,
            fnval: Int,
            bangumiPageMutableList: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>,
        ) {

            //向第三方统计提交数据
            addThirdPartyData(
                videoBaseBean.data.bvid,
                videoBaseBean.data.aid,
                videoBaseBean.data.owner.mid,
                videoBaseBean.data.owner.name,
                videoBaseBean.data.copyright,
                videoBaseBean.data.tname,
                downloadTool,
                downloadType,
                downloadCondition,
                toneQuality,
            )

            //首先判断是什么类型
            when (downloadType) {
                DASH_TYPE -> {
                    addBangumiDownloadTask(
                        context,
                        videoBaseBean,
                        qn,
                        80,
                        downloadTool,
                        downloadCondition,
                        toneQuality,
                        bangumiPageMutableList
                    )
                }
                MP4_TYPE -> {
                    addFlvBangumiDownloadTask(
                        context,
                        videoBaseBean,
                        qn,
                        80,
                        downloadTool,
                        bangumiPageMutableList
                    )
                }
            }
        }


        /**
         * 加载视频音质列表
         *  @param dashVideoPlayBean DashVideoPlayBean
         */
        private fun loadVideoToneQualityList(
            context: Context,
            dashVideoPlayBean: DashVideoPlayBean,
            selectedResult: (audio: DashVideoPlayBean.DataBean.DashBean.AudioBean) -> Unit,
        ): BottomSheetDialog {

            val binding = DialogToneQualityBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)

            initDialogBehaviorBinding(binding.dialogToneQualityBar, context, binding.root.parent)

            binding.apply {
                dialogToneQualityRv.adapter =
                    VideoToneQualityAdapter(dashVideoPlayBean.data.dash.audio, selectedResult)
                dialogToneQualityRv.layoutManager = LinearLayoutManager(context)

                dialogToneQualityFinishBt.setOnClickListener {
                    bottomSheetDialog.cancel()
                }
            }



            return bottomSheetDialog


        }


        /**
         * 缓存视频弹窗
         * @param context Context
         * @param videoBaseBean VideoBaseBean
         * @param videoPageListData VideoPageListData
         * @param dashVideoPlayBean DashVideoPlayBean
         * @return BottomSheetDialog
         */
        @SuppressLint("CommitPrefEdits")
        @JvmStatic
        fun downloadVideoDialog(
            context: Context,
            videoBaseBean: VideoBaseBean,
            videoPageListData: VideoPageListData,
            dashVideoPlayBean: DashVideoPlayBean,
        ): BottomSheetDialog {

            var videoPageMutableList = mutableListOf<VideoPageListData.DataBean>()
            var selectDefinition = 80
            var toneQuality = 30280

            var downloadType = DASH_TYPE
            var downloadTool = APP_DOWNLOAD
            var downloadCondition = VIDEOANDAUDIO

            videoPageMutableList.add(videoPageListData.data[0])

            val binding = DialogDownloadVideoBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)


            initDialogBehaviorBinding(binding.dialogDlVideoBar, context, binding.root.parent)
            //自定义方案
            //mDialogBehavior.peekHeight = 600
            binding.apply {

                //子集选择 默认选中1集
                videoPageListData.data[0].selected = 1
                dialogDlVideoDiversityLy.setOnClickListener {
                    loadVideoPageDialog(context, videoPageListData, videoPageMutableList) { it1 ->
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

                dialogDlVideoDefinitionLy.setOnClickListener {
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


                dialogDlVideoTypeLy.setOnClickListener {
                    loadDownloadTypeDialog(context) { selects, typeName ->
                        downloadType = selects
                        dialogDlVideoTypeTx.text = typeName
                    }.show()
                }


                //设置音质选择
                dialogDlAudioTypeTx.setOnClickListener {
                    loadVideoToneQualityList(context, dashVideoPlayBean) {
                        toneQuality = it.id
                        dialogDlAudioTypeTx.text = AsVideoNumUtils.getQualityName(toneQuality)
                    }.show()
                }


                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).apply {
                    when (getInt("user_download_tool_list", 1)) {
                        1 -> {
                            downloadTool = APP_DOWNLOAD
                            dialogDlVideoRadioGroup.check(R.id.dialog_dl_video_app_dl)
                        }
                        2 -> {
                            downloadTool = IDM_DOWNLOAD
                            dialogDlVideoRadioGroup.check(R.id.dialog_dl_video_idm_dl)
                        }
                        3 -> {
                            downloadTool = ADM_DOWNLOAD
                            dialogDlVideoRadioGroup.check(R.id.dialog_dl_video_adm_dl)
                        }
                    }


                    downloadType = getInt("user_download_type", 1)
                    when (getInt("user_download_type", 1)) {
                        1 -> {
                            dialogDlVideoTypeTx.text = "Dash"
                        }
                        2 -> {
                            dialogDlVideoTypeTx.text = "MP4"
                        }
                    }

                    downloadCondition = getInt("user_download_condition", 1)
                    when (getInt("user_download_condition", 1)) {
                        1 -> {
                            dialogDlConditionRadioGroup.check(R.id.dialog_dl_video_and_audio)
                        }
                        2 -> {
                            dialogDlConditionRadioGroup.check(R.id.dialog_dl_only_audio)
                        }
                        3 -> {
                            dialogDlConditionRadioGroup.check(R.id.dialog_dl_only_video)
                        }
                    }

                }


                dialogDlVideoRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
                    when (i) {
                        R.id.dialog_dl_video_idm_dl -> {
                            sharedPreferences.edit().putInt("user_download_tool_list", 2).apply()
                            downloadTool = IDM_DOWNLOAD
                        }
                        R.id.dialog_dl_video_app_dl -> {
                            sharedPreferences.edit().putInt("user_download_tool_list", 1).apply()
                            downloadTool = APP_DOWNLOAD
                        }
                        R.id.dialog_dl_video_adm_dl -> {
                            sharedPreferences.edit().putInt("user_download_tool_list", 3).apply()
                            downloadTool = ADM_DOWNLOAD
                        }
                    }
                }

                dialogDlConditionRadioGroup.setOnCheckedChangeListener { _, i ->
                    when (i) {
                        R.id.dialog_dl_video_and_audio -> {
                            downloadCondition = VIDEOANDAUDIO
                            sharedPreferences.edit {
                                putInt("user_download_condition", VIDEOANDAUDIO)
                                apply()
                            }
                        }
                        R.id.dialog_dl_only_video -> {
                            downloadCondition = ONLY_VIDEO
                            sharedPreferences.edit {
                                putInt("user_download_condition", ONLY_VIDEO)
                                apply()
                            }
                        }

                        R.id.dialog_dl_only_audio -> {
                            downloadCondition = ONLY_AUDIO
                            sharedPreferences.edit {
                                putInt("user_download_condition", ONLY_AUDIO)
                                apply()
                            }
                        }

                    }
                }


                dialogDlVideoButton.setOnClickListener {
                    downloadTaskStream(
                        context,
                        downloadType,
                        downloadTool,
                        downloadCondition,
                        toneQuality,
                        videoBaseBean,
                        selectDefinition,
                        80,
                        videoPageMutableList
                    )
                    bottomSheetDialog.cancel()

                }


            }


            return bottomSheetDialog

        }


        /**
         * 缓存番剧弹窗
         * @param context Context
         * @param videoBaseBean VideoBaseBean
         * @param bangumiSeasonBean BangumiSeasonBean
         * @param dashVideoPlayBean DashVideoPlayBean
         * @return BottomSheetDialog
         */
        @JvmStatic
        fun downloadVideoDialog(
            context: Context,
            videoBaseBean: VideoBaseBean,
            bangumiSeasonBean: BangumiSeasonBean,
            dashVideoPlayBean: DashVideoPlayBean,
        ): BottomSheetDialog {
            var videoPageMutableList = mutableListOf<BangumiSeasonBean.ResultBean.EpisodesBean>()
            var selectDefinition = 80

            var toneQuality = 30280

            var downloadType = DASH_TYPE
            var downloadTool = APP_DOWNLOAD
            var downloadCondition = VIDEOANDAUDIO


            videoPageMutableList.add(bangumiSeasonBean.result.episodes[0])


            val binding = DialogDownloadVideoBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //设置布局
            bottomSheetDialog.setContentView(binding.root)


            initDialogBehaviorBinding(binding.dialogDlVideoBar, context, binding.root.parent)


            //自定义方案
            //mDialogBehavior.peekHeight = 600
            binding.apply {
                //子集选择 默认选中1集
                bangumiSeasonBean.result.episodes[0].selected = 1
                dialogDlVideoDiversityLy.setOnClickListener {
                    loadVideoPageDialog(context, bangumiSeasonBean, videoPageMutableList) { it1 ->
                        videoPageMutableList = it1
                        var videoPageMsg = ""

                        if (videoPageMutableList.size != 1) {
                            videoPageMutableList.forEach {
                                videoPageMsg = "$videoPageMsg${it.long_title} "
                            }
                            dialogDlVideoDiversityTx.text = videoPageMsg
                        }
                    }.show()


                }

                //清晰度选择
                dialogDlVideoDefinitionLy.setOnClickListener {
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


                //下载类型选择
                dialogDlVideoTypeLy.setOnClickListener {
                    loadDownloadTypeDialog(context) { selects, typeName ->
                        downloadType = selects
                        dialogDlVideoTypeTx.text = typeName
                    }.show()
                }

                //设置音质选择
                dialogDlAudioTypeLy.setOnClickListener {
                    loadVideoToneQualityList(context, dashVideoPlayBean) {
                        toneQuality = it.id
                        dialogDlAudioTypeTx.text = AsVideoNumUtils.getQualityName(toneQuality)
                    }.show()
                }


                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).apply {
                    when (getInt("user_download_tool_list", 1)) {
                        1 -> {
                            dialogDlVideoRadioGroup.check(R.id.dialog_dl_video_app_dl)
                        }
                        2 -> {
                            dialogDlVideoRadioGroup.check(R.id.dialog_dl_video_idm_dl)
                        }
                        3 -> {
                            dialogDlVideoRadioGroup.check(R.id.dialog_dl_video_adm_dl)
                        }
                    }

                    downloadTool = getInt("user_download_tool_list", 1)
                    downloadType = getInt("user_download_type", 1)

                    when (getInt("user_download_type", 1)) {
                        1 -> {
                            dialogDlVideoTypeTx.text = "Dash"
                        }
                        2 -> {
                            dialogDlVideoTypeTx.text = "MP4"
                        }
                    }

                    downloadCondition = getInt("user_download_condition", 1)
                    when (getInt("user_download_condition", 1)) {
                        1 -> {
                            dialogDlConditionRadioGroup.check(R.id.dialog_dl_video_and_audio)
                        }
                        2 -> {
                            dialogDlConditionRadioGroup.check(R.id.dialog_dl_only_audio)
                        }
                        3 -> {
                            dialogDlConditionRadioGroup.check(R.id.dialog_dl_only_video)
                        }
                    }

                }


                dialogDlVideoRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
                    when (i) {
                        R.id.dialog_dl_video_idm_dl -> {
                            sharedPreferences.edit().putInt("user_download_tool_list", 2).apply()
                            downloadTool = IDM_DOWNLOAD
                        }
                        R.id.dialog_dl_video_app_dl -> {
                            sharedPreferences.edit().putInt("user_download_tool_list", 1).apply()
                            downloadTool = APP_DOWNLOAD
                        }
                        R.id.dialog_dl_video_adm_dl -> {
                            sharedPreferences.edit().putInt("user_download_tool_list", 3).apply()
                            downloadTool = ADM_DOWNLOAD
                        }
                    }
                }

                dialogDlConditionRadioGroup.setOnCheckedChangeListener { _, i ->
                    when (i) {
                        R.id.dialog_dl_video_and_audio -> {
                            downloadCondition = VIDEOANDAUDIO
                            sharedPreferences.edit {
                                putInt("user_download_condition", VIDEOANDAUDIO)
                                apply()
                            }
                        }
                        R.id.dialog_dl_only_video -> {
                            downloadCondition = ONLY_VIDEO
                            sharedPreferences.edit {
                                putInt("user_download_condition", ONLY_VIDEO)
                                apply()
                            }
                        }

                        R.id.dialog_dl_only_audio -> {
                            downloadCondition = ONLY_AUDIO
                            sharedPreferences.edit {
                                putInt("user_download_condition", ONLY_AUDIO)
                                apply()
                            }
                        }

                    }
                }


                dialogDlVideoButton.setOnClickListener {
                    downloadTaskStream(
                        context,
                        downloadType,
                        downloadTool,
                        downloadCondition,
                        toneQuality,
                        videoBaseBean,
                        selectDefinition,
                        80,
                        videoPageMutableList
                    )

                    bottomSheetDialog.cancel()
                }


            }


            return bottomSheetDialog

        }


        private fun addBangumiDownloadTask(
            context: Context,
            videoBaseBean: VideoBaseBean,
            qn: Int,
            fnval: Int,
            downloadTool: Int,
            downloadCondition: Int,
            toneQuality: Int,
            bangumiPageMutableList: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>,
        ) {
            bangumiPageMutableList.forEach {
                when (downloadCondition) {
                    VIDEOANDAUDIO -> {
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "video"
                        )
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "audio"
                        )
                    }
                    ONLY_AUDIO -> {
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "audio",
                            false
                        )
                    }

                    ONLY_VIDEO -> {
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "video",
                            false
                        )
                    }
                }

            }
        }


        private fun addDownloadTask(
            context: Context,
            videoBaseBean: VideoBaseBean,
            qn: Int,
            fnval: Int,
            downloadTool: Int,
            downloadCondition: Int,
            toneQuality: Int,
            videoPageMutableList: MutableList<VideoPageListData.DataBean>,
        ) {
            videoPageMutableList.forEach {

                when (downloadCondition) {
                    VIDEOANDAUDIO -> {
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "video"
                        )
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "audio"
                        )
                    }
                    ONLY_AUDIO -> {
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "audio",
                            false
                        )
                    }
                    ONLY_VIDEO -> {
                        addTask(
                            context,
                            it,
                            qn,
                            fnval,
                            videoBaseBean,
                            downloadTool,
                            toneQuality,
                            "video",
                            false
                        )
                    }
                }


            }
        }


        /**
         * 添加视频FLV下载任务
         * @param context Context
         * @param videoBaseBean VideoBaseBean
         * @param qn Int
         * @param fnval Int
         * @param videoPageMutableList MutableList<DataBean>
         */
        private fun addFlvDownloadTask(
            context: Context,
            videoBaseBean: VideoBaseBean,
            qn: Int,
            fnval: Int,
            downloadTool: Int,
            videoPageMutableList: MutableList<VideoPageListData.DataBean>,
        ) {
            videoPageMutableList.forEach {
                addFlvTask(context, it, qn, fnval, videoBaseBean, downloadTool, "video", false)
            }
        }


        /**
         * 添加番剧FLV下载任务
         * @param context Context
         * @param videoBaseBean VideoBaseBean
         * @param qn Int
         * @param fnval Int
         * @param bangumiPageMutableList MutableList<EpisodesBean>
         */
        private fun addFlvBangumiDownloadTask(
            context: Context,
            videoBaseBean: VideoBaseBean,
            qn: Int,
            fnval: Int,
            downloadTool: Int,
            bangumiPageMutableList: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>,
        ) {
            bangumiPageMutableList.forEach {
                addFlvTask(context, it, qn, fnval, videoBaseBean, downloadTool, "video", false)
            }
        }

        /**
         * 添加视频FLV下载任务
         * @param context Context
         * @param dataBean DataBean
         * @param qn Int
         * @param fnval Int
         * @param videoBaseBean VideoBaseBean
         * @param type String
         */
        private fun addFlvTask(
            context: Context,
            dataBean: VideoPageListData.DataBean,
            qn: Int,
            fnval: Int,
            videoBaseBean: VideoBaseBean,
            downloadTool: Int,
            type: String,
            isGroupTask: Boolean = false,
        ) {
            Toast.makeText(context, "已添加到下载队列", Toast.LENGTH_SHORT).show()


            HttpUtils.addHeader("cookie", (context as AbsActivity).asUser.cookie)
                .addHeader("referer", "https://www.bilibili.com")
                .get(
                    "${BilibiliApi.videoPlayPath}?bvid=${videoBaseBean.data.bvid}&cid=${dataBean.cid}&qn=$qn&fnval=0&fourk=1",
                    VideoPlayBean::class.java
                ) { it1 ->

                    val videoPlayData = it1.data
                    val urlIndex = 0

                    val intFileType: Int
                    val fileType: String
                    val url = when (type) {
                        "video" -> {
                            intFileType = 0
                            fileType = "mp4"
                            videoPlayData.durl[0].url
                        }
                        else -> throw IllegalArgumentException("Invalid type: $type")
                    }

                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val inputString =
                        sharedPreferences.getString(
                            "user_download_file_name_editText",
                            "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}"
                        )
                            .toString()
                    val savePath = inputString.toAsDownloadSavePath(
                        context,
                        videoBaseBean.data.aid.toString(),
                        videoBaseBean.data.bvid,
                        dataBean.part,
                        dataBean.cid.toString(),
                        fileType,
                        urlIndex.toString(),
                        videoBaseBean.data.title,
                        qn.toString(),
                    )


                    when (downloadTool) {

                        APP_DOWNLOAD -> {
                            App.downloadQueue.addTask(
                                url,
                                savePath,
                                intFileType,
                                DownloadTaskDataBean(
                                    dataBean.cid,
                                    dataBean.part,
                                    videoBaseBean.data.bvid,
                                    qn.toString(),
                                    videoPlayBean = it1,
                                    videoPageDataData = dataBean,
                                ),
                                isGroupTask = isGroupTask,
                            ) { it2 ->
                                if (it2) {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }

                        IDM_DOWNLOAD -> {
                            toIdmDownload(url, context)
                        }

                        ADM_DOWNLOAD -> {
                            toAdmDownload(url, context)
                        }

                    }
                }
        }


        /**
         * 添加番剧FLV下载任务
         * @param context Context
         * @param dataBean DataBean
         * @param qn Int
         * @param fnval Int
         * @param videoBaseBean VideoBaseBean
         * @param type String
         */
        private fun addFlvTask(
            context: Context,
            dataBean: BangumiSeasonBean.ResultBean.EpisodesBean,
            qn: Int,
            fnval: Int,
            videoBaseBean: VideoBaseBean,
            downloadTool: Int,
            type: String,
            isGroupTask: Boolean = false,
        ) {
           val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            HttpUtils.apply {
                if (sharedPreferences.getBoolean(
                        "use_roam_cookie_state",
                        true
                    )
                ) this.addHeader("cookie",  (context as AbsActivity).asUser.cookie)
            }
                .addHeader("referer", "https://www.bilibili.com")
                .get(
                    "${BaseApplication.roamApi}pgc/player/web/playurl?cid=${dataBean.cid}&qn=$qn&fnval=0&fourk=1",
                    BangumiPlayBean::class.java
                ) { it1 ->

                    val videoPlayData = it1.result
                    var urlIndex = 0
                    //获取视频
                    videoPlayData.accept_quality.forEachIndexed { index, i ->
                        if (i == qn) {
                            urlIndex = index
                            return@forEachIndexed
                        }
                    }

                    val intFileType: Int
                    val fileType: String
                    val url = when (type) {
                        "video" -> {
                            intFileType = 0
                            fileType = "mp4"
                            videoPlayData.durl[0].url
                        }
                        else -> throw IllegalArgumentException("Invalid type: $type")
                    }

                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val inputString =
                        sharedPreferences.getString(
                            "user_download_file_name_editText",
                            "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}"
                        )
                            .toString()

                    val savePath = inputString.toAsDownloadSavePath(
                        context,
                        videoBaseBean.data.aid.toString(),
                        videoBaseBean.data.bvid,
                        dataBean.long_title,
                        dataBean.cid.toString(),
                        fileType,
                        urlIndex.toString(),
                        videoBaseBean.data.title,
                        qn.toString(),
                    )

                    when (downloadTool) {
                        APP_DOWNLOAD -> {
                            App.downloadQueue.addTask(
                                url,
                                savePath,
                                intFileType,
                                DownloadTaskDataBean(
                                    dataBean.cid,
                                    dataBean.title,
                                    videoBaseBean.data.bvid,
                                    qn.toString(),
                                    bangumiPlayBean = it1,
                                    bangumiSeasonBean = dataBean,
                                ),
                                isGroupTask = isGroupTask,
                            ) { it2 ->
                                if (it2) {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        IDM_DOWNLOAD -> {
                            toIdmDownload(url, context)
                        }
                        ADM_DOWNLOAD -> {
                            toAdmDownload(url, context)
                        }
                    }
                }
        }

        /**
         * 添加番剧下载任务
         * @param context Context
         * @param dataBean EpisodesBean
         * @param qn Int
         * @param fnval Int
         * @param videoBaseBean VideoBaseBean
         * @param type String
         */
        private fun addTask(
            context: Context,
            dataBean: BangumiSeasonBean.ResultBean.EpisodesBean,
            qn: Int,
            fnval: Int,
            videoBaseBean: VideoBaseBean,
            downloadTool: Int,
            toneQuality: Int,
            type: String,
            isGroupTask: Boolean = true,
        ) {
            Toast.makeText(context, "已添加到下载队列", Toast.LENGTH_SHORT).show()
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            HttpUtils.apply {
                if (sharedPreferences.getBoolean(
                        "use_roam_cookie_state",
                        true
                    )
                ) this.addHeader("cookie",  (context as AbsActivity).asUser.cookie)
            }
                .addHeader("referer", "https://www.bilibili.com")
                .get(
                    "${BaseApplication.roamApi}pgc/player/web/playurl?cid=${dataBean.cid}&qn=$qn&fnval=4048&fourk=1",
                    DashBangumiPlayBean::class.java
                ) { it1 ->

                    val bangumiPlayData = it1.result
                    var urlIndex = 0
                    //获取视频/音频的索引
                    it1.result.dash.video.run {
                        forEachIndexed fe@{ index, i ->
                            if (i.id == qn) {
                                urlIndex = index
                                return@run
                            }
                        }
                    }

                    val intFileType: Int
                    val fileType: String
                    val url = when (type) {
                        "video" -> {
                            intFileType = 0
                            fileType = "mp4"
                            bangumiPlayData.dash.video[urlIndex].baseUrl
                        }
                        "audio" -> {
                            intFileType = 1
                            fileType = "m4a"
                            var mUrl = bangumiPlayData.dash.audio[0].baseUrl
                            bangumiPlayData.dash.audio.forEach {
                                if (it.id == toneQuality) mUrl = it.baseUrl
                            }
                            mUrl
                        }
                        else -> throw IllegalArgumentException("Invalid type: $type")
                    }

                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val inputString =
                        sharedPreferences.getString(
                            "user_download_file_name_editText",
                            "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}"
                        )
                            .toString()
                    val savePath = inputString.toAsDownloadSavePath(
                        context,
                        videoBaseBean.data.aid.toString(),
                        videoBaseBean.data.bvid,
                        dataBean.long_title,
                        dataBean.cid.toString(),
                        fileType,
                        urlIndex.toString(),
                        videoBaseBean.data.title,
                        qn.toString(),
                    )

                    when (downloadTool) {
                        APP_DOWNLOAD -> {
                            App.downloadQueue.addTask(
                                url,
                                savePath,
                                intFileType,
                                DownloadTaskDataBean(
                                    dataBean.cid,
                                    dataBean.long_title,
                                    videoBaseBean.data.bvid,
                                    qn.toString(),
                                    dashBangumiPlayBean = it1,
                                    bangumiSeasonBean = dataBean,
                                ),
                                isGroupTask = isGroupTask
                            ) { it2 ->
                                if (it2) {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        IDM_DOWNLOAD -> {
                            toIdmDownload(url, context)
                        }
                        ADM_DOWNLOAD -> {
                            toAdmDownload(url, context)
                        }
                    }


                }
        }


        /**
         * 添加视频下载任务
         * @param context Context
         * @param dataBean DataBean
         * @param qn Int
         * @param fnval Int
         * @param videoBaseBean VideoBaseBean
         * @param type String
         */
        private fun addTask(
            context: Context,
            dataBean: VideoPageListData.DataBean,
            qn: Int,
            fnval: Int,
            videoBaseBean: VideoBaseBean,
            downloadTool: Int,
            toneQuality: Int,
            type: String,
            isGroupTask: Boolean = true,
        ) {
            Toast.makeText(context, "已添加到下载队列", Toast.LENGTH_SHORT).show()

            HttpUtils.addHeader("cookie",  (context as AbsActivity).asUser.cookie)
                .addHeader("referer", "https://www.bilibili.com")
                .get(
                    "${BilibiliApi.videoPlayPath}?bvid=${videoBaseBean.data.bvid}&cid=${dataBean.cid}&qn=$qn&fnval=4048&fourk=1",
                    DashVideoPlayBean::class.java
                ) { it1 ->

                    val videoPlayData = it1.data
                    var urlIndex = 0
                    //获取视频/音频的索引
                    it1.data.dash.video.run {
                        forEachIndexed fe@{ index, i ->
                            if (i.id == qn) {
                                urlIndex = index
                                return@run
                            }
                        }
                    }

                    val intFileType: Int
                    val fileType: String
                    val url = when (type) {
                        "video" -> {
                            intFileType = 0
                            fileType = "mp4"
                            videoPlayData.dash.video[urlIndex].baseUrl
                        }
                        "audio" -> {
                            intFileType = 1
                            fileType = "m4a"
                            var mUrl = videoPlayData.dash.audio[0].baseUrl
                            videoPlayData.dash.audio.forEach {
                                if (it.id == toneQuality) mUrl = it.baseUrl
                            }
                            mUrl
                        }
                        else -> throw IllegalArgumentException("Invalid type: $type")
                    }

                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val inputString =
                        sharedPreferences.getString(
                            "user_download_file_name_editText",
                            "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}"
                        )
                            .toString()

                    //获取下载地址
                    val savePath = inputString.toAsDownloadSavePath(
                        context,
                        videoBaseBean.data.aid.toString(),
                        videoBaseBean.data.bvid,
                        dataBean.part,
                        dataBean.cid.toString(),
                        fileType,
                        urlIndex.toString(),
                        videoBaseBean.data.title,
                        qn.toString(),
                    )

                    when (downloadTool) {
                        APP_DOWNLOAD -> {
                            App.downloadQueue.addTask(
                                url,
                                savePath,
                                intFileType,
                                DownloadTaskDataBean(
                                    dataBean.cid,
                                    dataBean.part,
                                    videoBaseBean.data.bvid,
                                    qn.toString(),
                                    dashVideoPlayBean = it1,
                                    videoPageDataData = dataBean,
                                ),
                                isGroupTask = isGroupTask,
                            ) { it2 ->
                                if (it2) {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "${videoBaseBean.data.bvid}下载失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        IDM_DOWNLOAD -> {
                            toIdmDownload(url, context)
                        }
                        ADM_DOWNLOAD -> {
                            toAdmDownload(url, context)
                        }
                    }

                }
        }


        private fun toIdmDownload(url: String, context: Context) {
            val intent = Intent("android.intent.action.VIEW")
            intent.addCategory("android.intent.category.APP_BROWSER")
            intent.data = Uri.parse(url)
            intent.putExtra("Cookie",  (context as AbsActivity).asUser.cookie)
            intent.putExtra("Referer", "https://www.bilibili.com/")
            intent.putExtra(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"
            )
            if (AppFilePathUtils.isInstallApp(context, "idm.internet.download.manager.plus")
            ) {
                Toast.makeText(context, "正在拉起IDM", Toast.LENGTH_SHORT).show()
                // 下载调用
                val str2 = "idm.internet.download.manager.plus"
                intent.setClassName(str2, "idm.internet.download.manager.Downloader")
                context.startActivity(intent)
            } else if (AppFilePathUtils.isInstallApp(context, "idm.internet.download.manager")
            ) {
                Toast.makeText(context, "正在拉起IDM", Toast.LENGTH_SHORT).show()
                // 下载调用
                val str2 = "idm.internet.download.manager"
                intent.setClassName(str2, "idm.internet.download.manager.Downloader")
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "看起来你还没有安装下载器", Toast.LENGTH_SHORT).show()
            }
        }

        private fun toAdmDownload(url: String, context: Context) {
            val intent = Intent("android.intent.action.VIEW")
            intent.addCategory("android.intent.category.APP_BROWSER")
            intent.data = Uri.parse(url)
            intent.putExtra("Cookie",  (context as AbsActivity).asUser.cookie)
            intent.putExtra("Referer", "https://www.bilibili.com/")
            intent.putExtra(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"
            )
            if (AppFilePathUtils.isInstallApp(context, "com.dv.adm")) {
                Toast.makeText(context, "正在拉起ADM", Toast.LENGTH_SHORT).show()
                // 下载调用
                val str1 = "com.dv.adm"
                val str2 = "com.dv.adm.AEditor"
                intent.setClassName(str1, str2)
                context.startActivity(intent)
            } else if (AppFilePathUtils.isInstallApp(context, "com.dv.adm.pay")) {
                Toast.makeText(context, "正在拉起ADM PRO", Toast.LENGTH_SHORT).show()
                // 下载调用
                val str1 = "com.dv.adm.pay"
                val str2 = "com.dv.adm.pay.AEditor"
                intent.setClassName(str1, str2)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "看起来你还没有安装下载器", Toast.LENGTH_SHORT).show()
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
            videoPageMutableList: MutableList<VideoPageListData.DataBean>,
            finished: (selects: MutableList<VideoPageListData.DataBean>) -> Unit,
        ): BottomSheetDialog {

            val binding = DialogCollectionBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //创建设置布局
            bottomSheetDialog.setContentView(binding.root)

            //val mDialogBehavior =
            initDialogBehaviorBinding(
                binding.dialogCollectionBar,
                context,
                binding.root.parent
            )


            binding.apply {
                dialogCollectionTitle.text = "请选择视频子集"

                val pageData = mutableListOf<VideoPageListData.DataBean>() + videoPageListData.data

                dialogCollectionRv.adapter =
                    VideoPageAdapter(videoPageListData.data) { position, itemBinding ->
                        //这个接口是为了处理弹窗背景问题
                        //标签，判断这一次是否有重复
                        var tage = true
                        //这里加also标签为的是可以return掉forEachIndexed
                        videoPageMutableList.also {range->
                            range.forEachIndexed { index, dataBean ->
                                if (dataBean.cid == videoPageListData.data[position].cid) {
                                    tage = false
                                    itemBinding.dataBean?.selected = 0
                                    videoPageMutableList.removeAt(index)
                                    dialogCollectionRv.adapter?.notifyItemChanged(index)
                                    return@also
                                }
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


                dialogCollectionAllSelectBt.apply {
                    visibility = View.VISIBLE

                    setOnClickListener {
                        pageData.forEachIndexed { index, episodesBean ->
                            episodesBean.selected = 1
                            videoPageMutableList.add(episodesBean)
                            dialogCollectionRv.adapter?.notifyItemChanged(index)
                        }

                    }

                }
            }

            return bottomSheetDialog


        }


        /**
         * 加载番剧子集
         * @param context Context
         * @param bangumiSeasonBean BangumiSeasonBean
         * @param finished Function1<[@kotlin.ParameterName] MutableList<EpisodesBean>, Unit>
         * @return BottomSheetDialog
         */
        private fun loadVideoPageDialog(
            context: Context,
            bangumiSeasonBean: BangumiSeasonBean,
            videoPageMutableList: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>,
            finished: (selects: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>) -> Unit,
        ): BottomSheetDialog {
            val binding = DialogCollectionBinding.inflate(LayoutInflater.from(context))

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            //创建设置布局
            bottomSheetDialog.setContentView(binding.root)

            //val mDialogBehavior =
            initDialogBehaviorBinding(
                binding.dialogCollectionBar,
                context,
                binding.root.parent
            )


            binding.apply {

                dialogCollectionTitle.text = "请选择视频子集"

                val userVipState = (context as AsVideoActivity).userBaseBean.data.vip.status
                //会员判断
                val epData =
                    mutableListOf<BangumiSeasonBean.ResultBean.EpisodesBean>() + bangumiSeasonBean.result.episodes.filter {
                        !(userVipState != 1 && it.badge == "会员")
                    }


                dialogCollectionRv.adapter =
                    BangumiPageAdapter(bangumiSeasonBean.result.episodes.filter {
                        //没会员直接不展示
                        !(userVipState != 1 && it.badge == "会员")
                    }.toMutableList()) { position, itemBinding ->

                        //标签，判断这一次是否有重复 有重复就是false否则true
                        var tage = true
                        //这里加also标签为的是可以return掉forEachIndexed
                        videoPageMutableList.also { range ->
                            range.forEachIndexed { index, episodesBean ->
                                if (episodesBean.cid == bangumiSeasonBean.result.episodes[position].cid) {
                                    tage = false
                                    itemBinding.episodesBean?.selected = 0
                                    dialogCollectionRv.adapter?.notifyItemChanged(index)
                                    videoPageMutableList.removeAt(index)
                                    return@also
                                }
                            }
                        }



                        if (tage) {
                            itemBinding.episodesBean?.selected = 1
                            videoPageMutableList.add(bangumiSeasonBean.result.episodes[position])
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

                dialogCollectionAllSelectBt.apply {
                    visibility = View.VISIBLE

                    setOnClickListener {
                        epData.forEachIndexed { index, episodesBean ->
                            episodesBean.selected = 1
                            videoPageMutableList.add(episodesBean)
                            dialogCollectionRv.adapter?.notifyItemChanged(index)
                        }

                    }

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
            initDialogBehaviorBinding(
                binding.dialogCollectionBar,
                context,
                binding.root.parent
            )


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