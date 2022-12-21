package com.imcys.bilibilias.splash.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.home.ui.activity.HomeActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0
    private var isFirstLoaded = false
    private var delayedHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getSavePermissions()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        delayedHandler?.removeCallbacksAndMessages(null)

    }


    private fun getSavePermissions() {
        // 首先检查是否已经授予了储存权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                // 在这里可以弹出一个对话框来说明为什么需要此权限
                DialogUtils.dialog(
                    this,
                    "授权提示",
                    "使用BILIBILIAS就需要要同意此权限，因为缓存视频等需要向系统内储存权限。",
                    "同意授权",
                    "拒绝授权",
                    positiveButtonClickListener = {
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
                    },
                    negativeButtonClickListener = {
                        finishAll()
                    }
                ).show()
            } else {
                // 申请储存权限
                DialogUtils.dialog(
                    this,
                    "权限申请",
                    "BILIBILIAS为了储存下载并且储存内容，需要申请一下储存权限，可以放心授权。"
                            + "如果你是小米，可能看到的描述是获取相册等其他文件的权限，请不用担心，本软件开源发布，仅仅使用权限储存文件，"
                            + "除非您自己将下载路径改动到了相册，否则不会去读取相册内容。",
                    "确定授权",
                    "拒绝授权",
                    positiveButtonClickListener = {
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
                    },
                    negativeButtonClickListener = {
                        // 处理取消按钮点击事件
                        finishAll()
                    }
                ).show()


            }
        } else {
            toHome()
        }

    }

    private fun toHome() {
        // 如果已经授予了储存权限，则可以进行相应的操作
        if (!isFirstLoaded) {
            isFirstLoaded = true
            delayedHandler = Handler(Looper.getMainLooper())
            delayedHandler?.let {
                HandlerCompat.postDelayed(it, {
                    // 创建一个意图，说明我要跳转到那个活动界面。
                    val intent = Intent(this, HomeActivity::class.java)
                    // 跳转到主要活动。
                    startActivity(intent)
                    // 再来个跳转过度动画。
                    overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                    // 销毁当前活动。
                    finish()

                }, null, 800L)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_WRITE_EXTERNAL_STORAGE -> {
                // 如果权限被授予，则可以进行相应的操作
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 已经授予了储存权限，可以进行相应的操作
                    toHome()

                } else {
                    // 用户拒绝了权限请求，可以提醒用户为什么需要此权限
                }
                return
            }
        }
    }
}