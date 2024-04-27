package com.imcys.bilibilias.base.model.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.constant.SET_COOKIE
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils
import com.imcys.bilibilias.common.di.AsCookiesStorage
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class LoginQRModel @Inject constructor(
    private val networkService: NetworkService,
    private val asCookiesStorage: AsCookiesStorage
) : ViewModel() {

    private val TAG = this.javaClass.name

    var binding: DialogLoginQrBottomsheetBinding? = null
    var loginTip = ""

    lateinit var responseResult: (Int, LoginStateBean) -> Unit

    /**
     * 完成登录方法
     * @param view View
     * @param qrcode_key String
     */
    fun finishLogin(view: View, qrcode_key: String) {
        val bottomSheetDialog = view.context?.let { DialogUtils.loadDialog(it) }
        bottomSheetDialog?.show()

        viewModelScope.launchUI {
            val loginStateBean = networkService.biliUserLogin(qrcode_key)
            Log.i(TAG, "finishLogin: " + loginStateBean.message)

            bottomSheetDialog?.cancel()

            if (loginStateBean.data.code != 0) {
                // 展示登录结果
                val loginQRModel = binding?.loginQRModel
                loginQRModel?.loginTip = loginStateBean.data.message
                binding?.loginQRModel = loginQRModel
            }else{
                asCookiesStorage.saveCookies()
            }
            // 将登录完成事件返回给Fragment
            responseResult(loginStateBean.data.code, loginStateBean)

        }

    }

    /**
     * 重新加载二维码视图
     * @param view View
     * @param loginQrcodeDataBean DataBean
     */
    fun reloadLoginQR(loginQrcodeDataBean: LoginQrcodeBean.DataBean) {
        viewModelScope.launchUI {
            val loginQRData = networkService.getLoginQRData()
                .apply { data.url = URLEncoder.encode(data.url, "UTF-8") }

            binding?.dataBean = loginQRData.data
        }
    }

    fun downloadLoginQR(view: View, loginQrcodeDataBean: LoginQrcodeBean.DataBean) {
        Glide.with(view.context).asBitmap()
            .load("https://pan.misakamoe.com/qrcode/?url=" + loginQrcodeDataBean.url)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?,
                ) {
                    val photoDir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath,
                        "BILIBILIAS",
                    )
                    if (!photoDir.exists()) {
                        photoDir.mkdirs()
                    }
                    val fileName =
                        view.context.getString(R.string.app_LoginQRModel_downloadLoginQR_fileName)
                    val photo = File(photoDir, fileName)
                    try {
                        val fos = FileOutputStream(photo)
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        fos.flush()
                        fos.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    updatePhotoMedia(photo, view.context)
                    asToast(
                        view.context,
                        view.context.getString(R.string.app_LoginQRModel_downloadLoginQR_asToast),
                    )
                    goToQR(view)
                }
            })
    }

    // 更新图库
    private fun updatePhotoMedia(file: File, context: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
        intent.data = Uri.fromFile(file)
        context.sendBroadcast(intent)
    }

    fun goToQR(view: View) {
        val context = view.context
        val packName = "tv.danmaku.bili"
        if (AppFilePathUtils.isInstallApp(context, packName)) {
            Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan")).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.app_LoginQRModel_goToQR),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    /**
     * 登录成功后储存cookie等资源
     * @param loginStateBean LoginStateBean
     * @param response Response
     */
    @SuppressLint("CommitPrefEdits")
    fun loginSuccessOp(loginStateBean: LoginStateBean, response: Response) {
        val kv = MMKV.mmkvWithID("data")

        kv.encode("refreshToken", loginStateBean.data.refreshToken)

        var cookies = ""
        // 创建 Pattern 对象
        val patternSESSDATA = "SESSDATA=(.*?);"
        val rSESSDATA: Pattern = Pattern.compile(patternSESSDATA)
        val patternBiliJct = "bili_jct=(.*?);"
        val rBiliJct: Pattern = Pattern.compile(patternBiliJct)

        response.headers.values(SET_COOKIE).forEach {
            cookies += it
            var m = rSESSDATA.matcher(it)

            if (m.find()) {
                val groupStr = m.group(1)
                kv.encode("SESSDATA", groupStr)
            }

            m = rBiliJct.matcher(it)

            if (m.find()) {
                val groupStr = m.group(1)
                kv.encode("bili_jct", groupStr)
            }
        }
        kv.apply {
            encode(COOKIES, cookies)
            encode("refreshToken", loginStateBean.data.refreshToken)
        }
    }
}
