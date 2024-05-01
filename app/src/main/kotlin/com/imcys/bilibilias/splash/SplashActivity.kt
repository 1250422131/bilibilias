package com.imcys.bilibilias.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.databinding.ActivitySplashBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val layoutId: Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 安卓13废弃对写入权限检测
            // TODO 将准备改为SAF，届时不在对软件检查储存权限
            toHome()
        } else {
            getSavePermissions()
        }
        onBackPressedDispatcher.addCallback { moveTaskToBack(true) }
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
                applyPermission(getString(R.string.app_permission_application_msg))
            } else {
                applyPermission(getString(R.string.app_permission_application_text))
            }
        } else {
            toHome()
        }
    }

    private fun applyPermission(content: String) {
        DialogUtils.dialog(
            this,
            getString(R.string.app_permission_application_title),
            content,
            getString(R.string.app_permission_application_confirm),
            getString(R.string.app_permission_application_cancel),
            false,
            positiveButtonClickListener = {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
                )
            },
            negativeButtonClickListener = {
                finishAll()
            },
        ).show()
    }

    private fun toHome() {
        val handler = Handler(Looper.getMainLooper())
        HandlerCompat.postDelayed(handler, {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
            )
            finish()
        }, null, 1000)
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
        }
    }
}
