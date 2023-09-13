package com.imcys.bilibilias.splash.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.tencent.mmkv.MMKV

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    private fun getSavePermissions() {
        // 在这里可以弹出一个对话框来说明为什么需要此权限
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

    private fun toHome() {
        // 再来个跳转过度动画。
        overridePendingTransition(
            // android:fromAlpha="0.0" android:toAlpha="1.0" time: 500
            // android:fromAlpha="1.0" android:toAlpha="0.0" time: 400
            android.R.anim.fade_in,
            android.R.anim.fade_out,
        )
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
}
