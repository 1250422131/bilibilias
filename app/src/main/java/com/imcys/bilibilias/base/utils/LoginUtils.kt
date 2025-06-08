package com.imcys.bilibilias.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.event.LoginFinishEvent
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.login.view.LoginViewModel
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils.initDialogBehaviorBinding
import com.imcys.bilibilias.base.utils.DialogUtils.setPad
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.databinding.DialogLoginBottomsheetBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUtils @Inject constructor() {

    @Inject
    lateinit var networkService: NetworkService

    suspend fun checkLoginState(): Boolean {
        val result = runCatching {
            return@runCatching networkService.getMyUserData()
        }
        if (result.isFailure) return false

        return result.getOrNull()?.run {
            code == 0
        } ?: false
    }

    /**
     * 登录对话框
     * @param context Context
     */
    @SuppressLint("InflateParams")
    fun loginDialogCommonPage(
        context: FragmentActivity,
    ): BottomSheetDialog {
        val binding = DialogLoginBottomsheetBinding.inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        // 设置布局
        binding.loginViewModel =
            ViewModelProvider(
                context as HomeActivity,
            )[LoginViewModel::class.java]

        binding.apply {
            dialogLoginBiliQr.setOnClickListener {
                context.launchUI {
                    context.loadLogin()
                }
                bottomSheetDialog.cancel()
            }

            dialogLoginAs.setOnClickListener {
                asToast(context, context.getString(R.string.app_dialog_dialog_astoast_content))
            }
        }

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setPad()

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


    private suspend fun FragmentActivity.loadLogin() {
        // 自己会切换IO
        val loginQRData = networkService.getLoginQRData()
            .apply { data.url = URLEncoder.encode(data.url, "UTF-8") }
        var loginQRDialog: BottomSheetDialog? = null
        loginQRDialog = DialogUtils.loginQRDialog(
            this,
            loginQRData,
        ) { code: Int, _: LoginStateBean ->
            // 登陆成功
            if (code == 0) {
                launchUI {
                    loginQRDialog?.let {
                        initUserData(it)
                    }
                }
            }
        }.apply {
            show()
        }
    }

    // 初始化用户数据
    private suspend fun FragmentActivity.initUserData(loginDialog: BottomSheetDialog) {
        // mid
        val bottomSheetDialog = DialogUtils.loadDialog(this)
        bottomSheetDialog.show()

        val myUserData = withContext(Dispatchers.IO) {
            networkService.getMyUserData()
        }

        if (myUserData.code == 0) {
            // 提交
            BaseApplication.myUserData = myUserData.data
            loadUserData(myUserData, loginDialog)
        } else {
            asToast(this, "登录出现意外，请重新完成登录")
            this.loadLogin()
        }

        bottomSheetDialog.cancel()
    }

    /**
     * 检查用户是否登陆
     */
    private fun detectUserLogin() {
        launchUI {
            // 无论如何优先获取必要Token
            val myUserData = networkService.getMyUserData()

            if (myUserData.code != 0) {
//                DialogUtils.loginDialog(requireActivity())
//                    .show()
            } else {
                // 解除风控
                networkService.getBILIHome()
                BaseApplication.myUserData = myUserData.data
            }
        }
    }


    private fun FragmentActivity.loadUserData(
        myUserData: MyUserData,
        loginDialog: BottomSheetDialog
    ) {
        launchUI {
            EventBus.getDefault().post(LoginFinishEvent(myUserData.data.mid))
            val userInfoBean = networkService.getUserInfoData(myUserData.data.mid)

            // 这里需要储存下数据
            BaseApplication.dataKv.encode("mid", myUserData.data.mid)

            // 关闭登陆登陆弹窗
            loginDialog.cancel()
            // 加载用户弹窗
            DialogUtils.userDataDialog(this@loadUserData, userInfoBean).show()
        }
    }


}