package com.imcys.bilibilias.base.model.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.common.base.model.AuthQrCodeBean
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import okhttp3.Response
import java.io.File

@Deprecated("To AuthViewModel")
class LoginQRModel {

    var binding: DialogLoginQrBottomsheetBinding? = null
    var loginTip = ""

    lateinit var responseResult: (Int, LoginStateBean) -> Unit

    /**
     * 完成登录方法
     * @param view View
     * @param qrcode_key String
     */
    fun finishLogin(view: View, qrcode_key: String) {}

    /**
     * 重新加载二维码视图
     * @param view View
     * @param loginQrcodeDataBean DataBean
     */
    fun reloadLoginQR(loginQrcodeDataBean: AuthQrCodeBean.Data) {}

    fun downloadLoginQR(view: View, loginQrcodeDataBean: AuthQrCodeBean.Data) {}

    // 更新图库
    private fun updatePhotoMedia(file: File, context: Context) {}

    fun goToQR(view: View) {}

    /**
     * 登录成功后储存cookie等资源
     * @param loginStateBean LoginStateBean
     * @param response Response
     */
    @SuppressLint("CommitPrefEdits")
    fun loginSuccessOp(loginStateBean: LoginStateBean, response: Response) {}
}
