package com.imcys.bilibilias.splash.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.os.HandlerCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.liulishuo.okdownload.OkDownloadProvider.context
import com.tencent.mmkv.MMKV


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0
    private val MANAGE_EXTERNAL_STORAGE_REQUEST_CODE = 1

    @RequiresApi(Build.VERSION_CODES.R)
    private val MANAGE_EXTERNAL_STORAGES = arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE)

    private var isFirstLoaded = false
    private var delayedHandler: Handler? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // 首先检查是否已经授予了储存权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!Environment.isExternalStorageManager()) {
                DialogUtils.dialog(
                    this,
                    getString(R.string.app_permission_application_title),
                    "下面将授权所有文件访问权限，你可以不这么做，但是如果你自定义了下载存储路径，那么是无法在APP内唤起系统播放下载的视频的。",
                    getString(R.string.app_permission_application_confirm),
                    getString(R.string.app_permission_application_cancel),
                    false,
                    positiveButtonClickListener = {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                        )
                        intent.setData(Uri.parse("package:$packageName"))
                        startActivity(intent)
                        toHome()
                    },
                    negativeButtonClickListener = {
                    },
                ).show()

            } else {
                toHome()
            }
        } else {
            getSavePermissions()
        }

//        val constraintLayout = findViewById<ConstraintLayout>(R.id.splash_top)
//        constraintLayout.addStatusBarTopPadding()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        delayedHandler?.removeCallbacksAndMessages(null)
    }


    private fun getSavePermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
            ) {
                // 在这里可以弹出一个对话框来说明为什么需要此权限
                DialogUtils.dialog(
                    this,
                    getString(R.string.app_permission_application_title),
                    getString(R.string.app_permission_application_msg),
                    getString(R.string.app_permission_application_confirm),
                    getString(R.string.app_permission_application_cancel),
                    false,
                    positiveButtonClickListener = {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            ),
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
                        )
                    },
                    negativeButtonClickListener = {
                        finishAll()
                    },
                ).show()
            } else {
                // 申请储存权限
                DialogUtils.dialog(
                    this,
                    getString(R.string.app_permission_application_title),
                    getString(R.string.app_permission_application_text),
                    getString(R.string.app_permission_application_confirm),
                    getString(R.string.app_permission_application_cancel),
                    false,
                    positiveButtonClickListener = {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            ),
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
                        )
                    },
                    negativeButtonClickListener = {
                        // 处理取消按钮点击事件
                        finishAll()
                    },
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
                // 迁移旧的数据
                initMMVKData()

                HandlerCompat.postDelayed(it, {
                    // 创建一个意图，说明我要跳转到那个活动界面。
                    val intent = Intent(this, HomeActivity::class.java)
                    // 跳转到主要活动。
                    startActivity(intent)
                    // 再来个跳转过度动画。
                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                    )
                    // 销毁当前活动。
                    finish()
                }, null, 1000)
            }
        }
    }

    /**
     * 如果存在旧数据，意味着是旧版本的升级用户，这里进行数据迁移
     */
    private fun initMMVKData() {
        getSharedPreferences("data", MODE_PRIVATE).apply {
            if (!getString(COOKIES, "").equals("")) {
                MMKV.mmkvWithID("data")!!.importFromSharedPreferences(this)
                this.edit { clear() }
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
                toHome()
            }

            MANAGE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toHome()
                } else {
                    asToast(this, "自定义路径后你将无法直接跳转播放视频！")
                }
            }
        }
    }
}
