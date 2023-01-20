package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hyy.highlightpro.HighlightPro
import com.hyy.highlightpro.parameter.Constraints
import com.hyy.highlightpro.parameter.HighlightParameter
import com.hyy.highlightpro.parameter.MarginOffset
import com.hyy.highlightpro.shape.RectShape
import com.hyy.highlightpro.util.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.extend.toColorInt
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.databinding.FragmentHomeBinding
import com.imcys.bilibilias.databinding.TipAppBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.adapter.OldHomeBeanAdapter
import com.imcys.bilibilias.home.ui.model.OldHomeBannerDataBean
import com.imcys.bilibilias.home.ui.model.OldUpdateDataBean
import com.imcys.bilibilias.home.ui.model.view.FragmentHomeViewModel
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.distribute.Distribute
import com.xiaojinzi.component.anno.RouterAnno
import com.youth.banner.indicator.CircleIndicator
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.math.BigInteger
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.system.exitProcess

@RouterAnno(
    hostAndPath = ARouterAddress.AppHomeFragment,
)
class HomeFragment : Fragment() {

    lateinit var fragmentHomeBinding: FragmentHomeBinding;
    private lateinit var loginQRDialog: BottomSheetDialog
    private val bottomSheetDialog = context?.let { DialogUtils.loadDialog(it) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )


        //添加边距
        fragmentHomeBinding.apply {
            fragmentHomeTopLinearLayout.addStatusBarTopPadding()
            fragmentHomeViewModel = FragmentHomeViewModel()
        }


        initView()
        loadServiceData()

        return fragmentHomeBinding.root
    }

    override fun onResume() {
        super.onResume()
        val guideVersion = App.sharedPreferences.getString("AppGuideVersion", "")
        if (guideVersion != App.AppGuideVersion) {
            loadHomeGuide()
        }
        StatService.onPageStart(context, "HomeFragment")
    }


    /**
     * 加载引导
     */
    private fun loadHomeGuide() {

        HighlightPro.with(this)
            .setHighlightParameter {
                val tipAppBinding = TipAppBinding.inflate(LayoutInflater.from(context))
                tipAppBinding.tipAppTitle.text = "新版本内容在这里查看"
                HighlightParameter.Builder()
                    .setTipsView(tipAppBinding.root)
                    .setHighlightViewId(fragmentHomeBinding.fragmentHomeNewVersionLy.id)
                    .setHighlightShape(RectShape(4f.dp, 4f.dp, 6f))
                    .setHighlightHorizontalPadding(8f.dp)
                    .setConstraints(Constraints.TopToBottomOfHighlight + Constraints.EndToEndOfHighlight)
                    .setMarginOffset(MarginOffset(start = 8.dp))
                    .build()
            }
            .setBackgroundColor("#80000000".toColorInt())
            .setOnDismissCallback {

                (activity as HomeActivity).activityHomeBinding.homeViewPage.currentItem = 1
                (activity as HomeActivity).activityHomeBinding.homeBottomNavigationView.menu.getItem(
                    1).isCheckable = true

            }
            .show()

    }

    /**
     * 加载服务器信息
     */
    private fun loadServiceData() {
        loadAppData()
        loadBannerData()
    }

    /**
     * 加载轮播图信息
     */
    private fun loadBannerData() {

        HttpUtils.get("${BiliBiliAsApi.updateDataPath}?type=banner",
            OldHomeBannerDataBean::class.java) {

            //新增BannerLifecycleObserver
            fragmentHomeBinding.fragmentHomeBanner.setAdapter(
                OldHomeBeanAdapter(
                    it.textList,
                    it
                )
            )
                .setIndicator(CircleIndicator(context))
        }

    }

    /**
     * 加载APP数据
     */
    private fun loadAppData() {
        AppCenter.start((context as Activity).application, App.appSecret, Distribute::class.java)

        lifecycleScope.launch {

            val oldUpdateDataBean =
                HttpUtils.asyncGet("${BiliBiliAsApi.updateDataPath}?type=json&version=${BiliBiliAsApi.version}",
                    OldUpdateDataBean::class.java)

            //加载公告
            if (oldUpdateDataBean.notice != "") {
                loadNotice(oldUpdateDataBean.notice.toString())
            }
            //送出签名信息
            val sha = apkVerifyWithSHA(requireContext(), "")
            val md5 = apkVerifyWithMD5(requireContext(), "")
            val crc = apkVerifyWithCRC(requireContext(), "")

            when (oldUpdateDataBean.id) {
                "0" -> postAppData(sha, md5, crc)
                "1" -> checkAppData(oldUpdateDataBean, sha, md5, crc)
            }

            //检测更新
            loadVersionData(oldUpdateDataBean)

        }

    }


    /**
     * 加载版本信息
     */
    private fun loadVersionData(oldUpdateDataBean: OldUpdateDataBean) {
        if (BiliBiliAsApi.version.toString() != oldUpdateDataBean.version) {
            DialogUtils.dialog(
                requireContext(),
                "有新版本了",
                oldUpdateDataBean.gxnotice,
                "更新",
                "还是更新",
                oldUpdateDataBean.id != "3",
                positiveButtonClickListener = {
                    val uri = Uri.parse(oldUpdateDataBean.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri);
                    requireContext().startActivity(intent)
                },
                negativeButtonClickListener = {
                    val uri = Uri.parse(oldUpdateDataBean.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri);
                    requireContext().startActivity(intent)
                }
            ).show()
        }
    }

    /**
     * 核对APP数据
     * @param it OldUpdateDataBean
     * @param sha String
     * @param md5 String
     * @param crc String
     */
    private fun checkAppData(it: OldUpdateDataBean, sha: String, md5: String, crc: String) {
        if (it.apkmD5 != sha || it.apkToKenCR != crc || it.apkToKen != md5) {
            (activity as HomeActivity).activityHomeBinding.homeViewPage.visibility = View.GONE
            val uri: Uri = Uri.parse(it.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            exitProcess(0)
        }
    }

    /**
     * 送出此版本的数据信息
     */
    private fun postAppData(sha: String, md5: String, crc: String) {

        HttpUtils.get("${BiliBiliAsApi.updateDataPath}?type=json&version=${BiliBiliAsApi.version}" + "&SHA=" + sha + "&MD5=" + md5 + "&CRC=" + crc + "lj=" + LJ,
            OldUpdateDataBean::class.java) {
        }
    }

    /**
     * 加载公告
     * @param notice String
     */
    private fun loadNotice(notice: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val mNotice = sharedPreferences.getString("AppNotice", "")
        if (mNotice != notice) {
            DialogUtils.dialog(requireContext(),
                "最新通知",
                notice,
                "我明白了",
                "这份公告不行啊",
                true,
                positiveButtonClickListener = {
                    sharedPreferences.edit().putString("AppNotice", notice).apply()
                },
                negativeButtonClickListener = {
                }
            ).show()
        }

    }


    //初始化列表
    private fun initView() {

        //登陆检测
        //context?.let { DialogUtils.loginDialog(it).show() }
        //数据获取
        initUserToken()
        //加载推荐视频
        //loadRCMDVideoData()
        //检测用户是否登陆
        detectUserLogin()

        //loadRoamData()


    }

    /**
     * 加载漫游数据
     */
    private fun loadRoamData() {

        val roamId = App.sharedPreferences.getInt("use_roam_id", -1)
        if (roamId != -1) {
            queryRoamData(roamId)
        }

    }

    private fun queryRoamData(roamId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val roamDao = AppDatabase.getDatabase(requireContext()).roamDao()
            roamDao.getByIdQuery(roamId).apply {
                BilibiliApi.roamApi = romaPath
            }

        }
    }


    /**
     * 获取下之前登陆的用户信息
     */
    private fun initUserToken() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)

        App.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        BaseApplication.sharedPreferences = App.sharedPreferences
        //获取重要数据
        BaseApplication.cookies = sharedPreferences.getString("cookies", "").toString()
        BaseApplication.sessdata = sharedPreferences.getString("SESSDATA", "").toString()
        BaseApplication.biliJct = sharedPreferences.getString("bili_jct", "").toString()
        BaseApplication.mid = sharedPreferences.getLong("mid", 0)
    }

    /**
     * 加载登陆对话框
     */
    internal fun loadLogin() {
        HttpUtils.get(BilibiliApi.getLoginQRPath, LoginQrcodeBean::class.java) {
            it.data.url = URLEncoder.encode(it.data.url, "UTF-8")
            loginQRDialog = DialogUtils.loginQRDialog(
                context as Activity,
                it
            ) { code: Int, _: LoginStateBean ->
                //登陆成功
                if (code == 0) {
                    initUserData()
                    startStatistics()
                }
            }.apply {
                show()
            }

        }
    }

    /**
     * 启动统计
     */
    private fun startStatistics() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit()
            .putBoolean("microsoft_app_center_type", true)
            .putBoolean("baidu_statistics_type", true)
            .apply()
    }

    //初始化用户数据
    private fun initUserData() {

        bottomSheetDialog?.show()
        HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .get(
                BilibiliApi.getMyUserData, MyUserData::class.java
            ) {
                BaseApplication.myUserData = it.data
                loadUserData(it)
            }
    }

    /**
     * 检查用户是否登陆
     */
    private fun detectUserLogin() {

        HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .get(
                BilibiliApi.getMyUserData, MyUserData::class.java
            ) {
                //判断是否登陆，没有就加载登陆
                if (it.code != 0) DialogUtils.loginDialog(requireContext())
                    .show() else BaseApplication.myUserData = it.data
            }
    }


    //加载用户数据
    @SuppressLint("CommitPrefEdits")
    private fun loadUserData(myUserData: MyUserData) {

        HttpUtils
            .addHeader("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54")
            .addHeader("cookie", BaseApplication.cookies)
            .get(
                "${BilibiliApi.getUserInfoPath}?mid=${myUserData.data.mid}",
                UserInfoBean::class.java
            ) {
                //这里需要储存下数据
                val sharedPreferences =
                    requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putLong("mid", it.data.mid)
                //提交储存
                editor.apply()
                //这里给全局设置必要信息
                BaseApplication.mid = it.data.mid
                //关闭登陆登陆弹窗
                loginQRDialog.cancel()
                //加载用户弹窗
                DialogUtils.userDataDialog(requireActivity(), it).show()
            }

    }


    companion object {

        private var LJ: Int = 0

        @JvmStatic
        fun newInstance() = HomeFragment()

        //底层程序加固 -> 防止程序被修改从多个角度检测安装包完整性

        //底层程序加固 -> 防止程序被修改从多个角度检测安装包完整性
        /**
         * 通过检查签名文件classes.dex文件的哈希值来判断代码文件是否被篡改
         *
         * @param orginalSHA 原始Apk包的SHA-1值
         */
        fun apkVerifyWithSHA(context: Context, orginalSHA: String): String {
            val apkPath = context.packageCodePath // 获取Apk包存储路径
            try {
                val dexDigest: MessageDigest = MessageDigest.getInstance("SHA-1")
                val bytes = ByteArray(1024)
                var byteCount: Int
                val fis = FileInputStream(File(apkPath)) // 读取apk文件
                while (fis.read(bytes).also { byteCount = it } != -1) {
                    dexDigest.update(bytes, 0, byteCount)
                }
                val bigInteger = BigInteger(1, dexDigest.digest()) // 计算apk文件的哈希值
                val sha: String = bigInteger.toString(16)
                fis.close()
                return sha
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return orginalSHA
        }

        /**
         * 通过检查apk包的MD5摘要值来判断代码文件是否被篡改
         *
         * @param orginalMD5 原始Apk包的MD5值
         */
        fun apkVerifyWithMD5(context: Context, orginalMD5: String): String {
            val apkPath = context.packageCodePath // 获取Apk包存储路径
            LJ = apkPath.length
            try {
                val dexDigest: MessageDigest = MessageDigest.getInstance("MD5")
                val bytes = ByteArray(1024)
                var byteCount: Int
                val fis = FileInputStream(File(apkPath)) // 读取apk文件
                while (fis.read(bytes).also { byteCount = it } != -1) {
                    dexDigest.update(bytes, 0, byteCount)
                }
                val bigInteger = BigInteger(1, dexDigest.digest()) // 计算apk文件的哈希值
                val sha: String = bigInteger.toString(16)
                fis.close()
                return sha
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return orginalMD5
        }

        /**
         * 通过检查classes.dex文件的CRC32摘要值来判断文件是否被篡改
         *
         * @param orginalCRC 原始classes.dex文件的CRC值
         */
        fun apkVerifyWithCRC(context: Context, orginalCRC: String): String {
            val apkPath = context.packageCodePath // 获取Apk包存储路径
            try {
                val zipFile = ZipFile(apkPath)
                val dexEntry: ZipEntry = zipFile.getEntry("classes.dex") // 读取ZIP包中的classes.dex文件
                return dexEntry.crc.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return orginalCRC
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, "HomeFragment")
    }
}