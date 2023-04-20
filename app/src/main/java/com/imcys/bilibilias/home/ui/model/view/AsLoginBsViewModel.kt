package com.imcys.bilibilias.home.ui.model.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.model.common.IPostBody
import com.imcys.bilibilias.common.base.model.user.*
import com.imcys.bilibilias.common.base.utils.AESUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.databinding.DialogAsAccountListBinding
import com.imcys.bilibilias.databinding.DialogAsLoginBottomsheetBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.adapter.BiliBiliCookieAdapter
import com.imcys.bilibilias.home.ui.model.UserNavDataModel
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.util.regex.Pattern

class AsLoginBsViewModel(
    private val asLoginBottomsheetBinding: DialogAsLoginBottomsheetBinding,
    private val bottomSheetDialog: BottomSheetDialog,
    val finish: () -> Unit,
) :
    ViewModel() {


    /**
     * 刷新验证码
     * @param view View
     */
    fun refreshVerification(view: View) {

        //添加验证码 -> 很蠢的办法
        val asCookie =  BaseApplication.dataKv.decodeString("as_cookies")


        refresCerifictionCode(asCookie, view)

        asToast(view.context, "验证码已经更新")

    }

    /**
     * 刷新验证码具体操作
     * @param asCookie String?
     * @param view View
     */
    private fun refresCerifictionCode(asCookie: String?, view: View) {
        val glideUrl = GlideUrl(
            "${BiliBiliAsApi.serviceTestApi}users/getCaptchaImage?${System.currentTimeMillis()}",
            LazyHeaders.Builder()
                .addHeader("Cookie", asCookie!!)
                .build()
        )


        Glide.with(view.context)
            .load(glideUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 不缓存任何图片，即禁用磁盘缓存
            .error(R.mipmap.ic_launcher)
            .into(asLoginBottomsheetBinding.dgAsLoginVerificationImage)
    }

    private data class AsLoginInfo(
        val email: String,
        val password: String,
        val security: String,
    ) : IPostBody


    /**
     * 输入完成点击登录
     * @param view View
     */
    fun loginFinish(view: View) {


        val loadDialog = DialogUtils.loadDialog(view.context).apply { show() }

        viewModelScope.launch(Dispatchers.IO) {
            //构建登录
            val asLoginInfo = AsLoginInfo(
                asLoginBottomsheetBinding.dgAsLoginEmailEditText.text.toString(),
                asLoginBottomsheetBinding.dgAsLoginPwEditText.text.toString(),
                asLoginBottomsheetBinding.dgAsLoginVerificationEditText.text.toString()
            )

            val asCookie =  BaseApplication.dataKv.decodeString("as_cookies")

            val asUserLoginModel = KtHttpUtils.addHeader("cookie",asCookie!!).asyncPostJson<AsUserLoginModel>(
                "${BiliBiliAsApi.serviceTestApi}users/login",
                asLoginInfo
            )


            launch(Dispatchers.Main) {
                //判断登录
                if (asUserLoginModel.code == 0) {
                    //关闭加载对话框
                    loadDialog.cancel()
                    //登录成功
                     var asCookie =  BaseApplication.dataKv.decodeString("as_cookies")
                    asCookie += KtHttpUtils.setCookies
                    asCookie += ";"



                    //储存cookie
                    BaseApplication.dataKv.encode("as_cookies", asCookie)


                    (view.context as HomeActivity).asUser.asCookie = asCookie!!


                    finish()
                    //关闭当前弹窗
                    bottomSheetDialog.cancel()
                    //展示用户数据
                    showAccountList(view).show()
                } else {
                    val asCookie = BaseApplication.dataKv.decodeString("as_cookies")
                    refresCerifictionCode(asCookie, view)
                }

                asToast(view.context, asUserLoginModel.msg)
            }
        }

    }


    /**
     * 展示用户数据
     * @param view View
     * @return BottomSheetDialog
     */
    private fun showAccountList(view: View): BottomSheetDialog {
        //先获取View实例
        val binding = DialogAsAccountListBinding.inflate(LayoutInflater.from(view.context))

        val bottomSheetDialog = BottomSheetDialog(view.context, R.style.BottomSheetDialog)
        //设置布局
        bottomSheetDialog.setContentView(binding.root)
        binding.dialogAsAccountButton.setOnClickListener {
            addCloudAccount(view)
            bottomSheetDialog.cancel()
        }

        //设置item点击事件
        val biliBiliCookieAdapter = BiliBiliCookieAdapter() { _, data ->

            AsDialog.init(view.context).build {
                config = {
                    title = "操作事件"
                    content = "确定要选择这个账户？"
                    positiveButtonText = "是的"
                    positiveButton = {
                        checkUserData(data, bottomSheetDialog, view)
                        it.cancel()
                    }
                }
            }.show()


        }

        binding.dialogAsAccountRv.apply {
            adapter = biliBiliCookieAdapter
            layoutManager = LinearLayoutManager(context)
        }

        DialogUtils.initDialogBehaviorBinding(
            binding.dialogAsAccountBar,
            view.context,
            binding.root.parent
        )


        viewModelScope.launch {
            val asCookie = BaseApplication.dataKv.decodeString("as_cookies")


            val userBiliBiliCookieModel =
                KtHttpUtils.addHeader("Cookie", asCookie!!)
                    .asyncGet<UserBiliBiliCookieModel>("${BiliBiliAsApi.serviceTestApi}BiliBiliCookie")

            biliBiliCookieAdapter.submitList(userBiliBiliCookieModel.data)
        }


        return bottomSheetDialog
    }

    /**
     * 检查用户是否登录
     * @param data Data
     * @param bottomSheetDialog BottomSheetDialog
     * @param view View
     */
    private fun checkUserData(
        data: UserBiliBiliCookieModel.Data,
        bottomSheetDialog: BottomSheetDialog,
        view: View,
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val cookies = AESUtils.decrypt(data.cookie)
            val myUserData = KtHttpUtils.addHeader("cookie", cookies)
                .asyncGet<MyUserData>(
                    BilibiliApi.getMyUserData,
                )

            launch(Dispatchers.Main) {
                if (myUserData.code != 0) {
                    loadCloudAccountLogin(view.context)
                    deleteCloudAccount(view.context, data)
                    asToast(view.context, "账户失效,为您跳转到添加账户页面")
                } else {
                    //为0则代表用户数据仍然有效果，开始为全局设置
                    saveBiliCookieData(view.context, cookies)
                }

                bottomSheetDialog.cancel()

            }

        }


    }


    /**
     * 由于用户cookie失效，这里干掉服务器上的cookie
     * @param context Context?
     */
    private fun deleteCloudAccount(context: Context, data: UserBiliBiliCookieModel.Data) {
        viewModelScope.launch {
            val asCookie = BaseApplication.dataKv.decodeString("as_cookies")

            KtHttpUtils.addHeader("cookie", asCookie!!).asyncDeleteJson<ResponseResult>(
                "${BiliBiliAsApi.serviceTestApi}BiliBiliCookie",
                data
            )

        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveBiliCookieData(context: Context, cookie: String) {
        viewModelScope.launch {

            //Cookie过滤操作


            val kv = BaseApplication.dataKv

            // 创建 Pattern 对象
            val patternSESSDATA = "SESSDATA=(.*?);"
            val rSESSDATA: Pattern = Pattern.compile(patternSESSDATA)
            val patternBiliJct = "bili_jct=(.*?);"
            val rBiliJct: Pattern = Pattern.compile(patternBiliJct)


            //正则表达式检验
            var m = rSESSDATA.matcher(cookie)

            if (m.find()) {
                val groupStr = m.group(1)
                kv.encode("SESSDATA", groupStr)
            }

            m = rBiliJct.matcher(cookie)

            if (m.find()) {
                val groupStr = m.group(1)
                kv.encode("bili_jct", groupStr)
            }

            //cookies储存
            kv.encode("cookies", cookie)


            //获取用户数据
            val userNavDataModel =
                KtHttpUtils.addHeader(HttpHeaders.Cookie, cookie)
                    .asyncGet<UserNavDataModel>(BilibiliApi.userNavDataPath)

            //储存
            kv.apply {
                encode("mid", userNavDataModel.data.mid)
            }


        }


    }


    /**
     * 添加账户
     * @param view View
     */
    private fun addCloudAccount(view: View) {
        loadCloudAccountLogin(view.context)
    }

    /**
     * 加载登陆对话框
     */
    private fun loadCloudAccountLogin(context: Context) {
        viewModelScope.launch {
            val loginQrcodeBean = KtHttpUtils.asyncGet<LoginQrcodeBean>(BilibiliApi.getLoginQRPath)
            loginQrcodeBean.data.url =
                withContext(Dispatchers.IO) {
                    URLEncoder.encode(loginQrcodeBean.data.url, "UTF-8")
                }
            val loginQRDialog = DialogUtils.loginQRDialog(
                context,
                loginQrcodeBean
            ) { code: Int, _: LoginStateBean ->
                //登陆成功
                if (code == 0) {
                    (context as HomeActivity).homeFragment.initUserData()
                    context.homeFragment.startStatistics()
                    //提交云端资料
                    postCloudCookie(context)
                } else {
                    asToast(context, "登录异常")
                }
            }.apply {
                show()
            }

            (context as HomeActivity).homeFragment.loginQRDialog = loginQRDialog
        }

    }

    /**
     * 提交云端cookie
     * @param context HomeActivity
     */

    private data class BiliBiliCookieInfo(
        val name: String,
        val level: Int,
        val face: String,
        val cookie: String,
        val type: Int = 1,
    ) : IPostBody


    private fun postCloudCookie(context: HomeActivity) {
        viewModelScope.launch {


            //获取用户数据
            val userNavDataModel = KtHttpUtils.addHeader(HttpHeaders.Cookie, context.asUser.cookie)
                .asyncGet<UserNavDataModel>(BilibiliApi.userNavDataPath)


            val biliBiliCookieInfo = BiliBiliCookieInfo(
                userNavDataModel.data.uname,
                userNavDataModel.data.levelInfo.currentLevel,
                userNavDataModel.data.face,
                AESUtils.encrypt(context.asUser.cookie)
            )


            //提交云端 采用默认数据
            KtHttpUtils.addHeader("cookie", context.asUser.asCookie)
                .asyncPostJson<BiLiCookieResponseModel>(
                    "${BiliBiliAsApi.serviceTestApi}BiliBiliCookie",
                    biliBiliCookieInfo
                )


        }

    }


}