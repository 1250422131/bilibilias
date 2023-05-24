package com.imcys.bilibilias.base.model.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder
import java.util.regex.Pattern


class LoginQRModel {


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


        //登录完成
        HttpUtils.get(
            BilibiliApi.getLoginStatePath + "?qrcode_key=" + qrcode_key,
            object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    bottomSheetDialog?.cancel()
                }

                //————————————————————————————————————————————————
                @SuppressLint("CommitPrefEdits")
                override fun onResponse(call: Call, response: Response) {
                    //数据解析
                    val loginStateBean: LoginStateBean =
                        Gson().fromJson(response.body?.string(), LoginStateBean::class.java)
                    //————————————————————————————————————————————————
                    //关闭加载弹窗
                    bottomSheetDialog?.cancel()
                    //更新UI线程
                    BaseApplication.handler.post {
                        //登录成功则去储存cookie
                        if (loginStateBean.data.code == 0) {
                            loginSuccessOp( loginStateBean, response)
                        } else {
                            //展示登录结果
                            val loginQRModel = binding?.loginQRModel!!
                            loginQRModel.loginTip = loginStateBean.data.message
                            binding?.loginQRModel = loginQRModel

                        }
                        //将登录完成事件返回给Fragment
                        responseResult(loginStateBean.data.code, loginStateBean)
                    }


                }
            }
        )


    }


    /**
     * 重新加载二维码视图
     * @param view View
     * @param loginQrcodeDataBean DataBean
     */
    fun reloadLoginQR(loginQrcodeDataBean: LoginQrcodeBean.DataBean) {

        HttpUtils.get(
            BilibiliApi.getLoginQRPath,
            LoginQrcodeBean::class.java
        ) {
            loginQrcodeDataBean.url = URLEncoder.encode(it.data.url, "UTF-8")
            loginQrcodeDataBean.qrcode_key = it.data.qrcode_key
            binding?.dataBean = loginQrcodeDataBean
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
                    val photoDir = File(Environment.getExternalStorageDirectory(), "BILIBILIAS")
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
                        view.context.getString(R.string.app_LoginQRModel_downloadLoginQR_asToast)
                    )
                    goToQR(view)
                }
            })


    }


    //更新图库
    private fun updatePhotoMedia(file: File, context: Context) {
        val intent = Intent()
        intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
        intent.data = Uri.fromFile(file)
        context.sendBroadcast(intent)
    }

    fun goToQR(view: View) {
        val context = view.context
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan"))
            val packageManager = context.packageManager
            val componentName = intent.resolveActivity(packageManager)
            if (componentName != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.app_LoginQRModel_goToQR),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

        kv.encode("refreshToken", loginStateBean.data.refresh_token)

        var cookies = ""
        // 创建 Pattern 对象
        val patternSESSDATA = "SESSDATA=(.*?);"
        val rSESSDATA: Pattern = Pattern.compile(patternSESSDATA)
        val patternBiliJct = "bili_jct=(.*?);"
        val rBiliJct: Pattern = Pattern.compile(patternBiliJct)


        response.headers.values("Set-Cookie").forEach {
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
            encode("cookies", cookies)
            encode("refreshToken", loginStateBean.data.refresh_token)
        }


    }

}