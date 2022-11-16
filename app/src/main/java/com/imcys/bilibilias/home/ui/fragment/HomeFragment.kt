package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.user.MyUserData
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.databinding.FragmentHomeBinding
import com.imcys.bilibilias.home.ui.fragment.home.adapter.RCMDVideoAdapter
import com.imcys.bilibilias.home.ui.model.HomeRCMDVideoBean
import com.imcys.bilibilias.utils.HttpUtils
import com.youth.banner.transformer.ZoomOutPageTransformer
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import java.net.URLEncoder


class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding;
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
        fragmentHomeBinding.fragmentHomeTopLinearLayout.addStatusBarTopPadding()


        initView()

        return fragmentHomeBinding.root
    }

    //初始化列表
    private fun initView() {

        //登陆检测
        //context?.let { DialogUtils.loginDialog(it).show() }

        //数据获取
        initUserToken()

        //加载推荐视频
        loadRCMDVideoData()

        //测试登录
        testLogin()


    }

    private fun initUserToken() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
        //获取重要数据
        App.cookies = sharedPreferences.getString("cookies", "").toString()
        App.sessdata = sharedPreferences.getString("SESSDATA", "").toString()
        App.biliJct = sharedPreferences.getString("bili_jct", "").toString()
        App.mid = sharedPreferences.getLong("mid", 0)
    }

    private fun testLogin() {
        HttpUtils.get(BilibiliApi.getLoginQRPath, LoginQrcodeBean::class.java) {
            it.data.url = URLEncoder.encode(it.data.url, "UTF-8")
            loginQRDialog = DialogUtils.loginQRDialog(
                requireActivity(),
                it
            ) { code: Int, _: LoginStateBean ->
                //登陆成功
                if (code == 0) {
                    initUserData()
                }
            }.apply {
                show()
            }

        }
    }

    //初始化用户数据
    private fun initUserData() {

        bottomSheetDialog?.show()
        HttpUtils.addHeader("cookie", App.cookies)
            .get(
                BilibiliApi.getMyUserData, MyUserData::class.java
            ) {
                loadUserData(it)
            }
    }

    //加载用户数据
    @SuppressLint("CommitPrefEdits")
    private fun loadUserData(myUserData: MyUserData) {

        HttpUtils.addHeader("cookie", App.cookies)
            .get(
                BilibiliApi.getUserInfoPath + "?mid=" + myUserData.data.mid,
                UserInfoBean::class.java
            ) {
                val sharedPreferences: SharedPreferences =
                    requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putLong("mid", it.data.mid)
                editor.apply()
                App.mid = it.data.mid
                loginQRDialog.cancel()
                DialogUtils.userDataDialog(requireActivity(), it).show()
            }

    }


    //加载推荐视频
    private fun loadRCMDVideoData() {
        HttpUtils.addHeader("cookie", App.cookies).get(
            BilibiliApi.homeRCMDVideoPath + "?ps=10",
            HomeRCMDVideoBean::class.java
        ) {
            fragmentHomeBinding.run {
                //新增BannerLifecycleObserver
                fragmentHomeBanner.addBannerLifecycleObserver(activity)
                    .setAdapter(
                        RCMDVideoAdapter(
                            requireActivity(),
                            it.data.item
                        )
                    )
                    .setBannerGalleryEffect(5, 5, 40)
                    .addPageTransformer(ZoomOutPageTransformer())
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}