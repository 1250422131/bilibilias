package com.imcys.bilibilias.splash.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.home.ui.activity.HomeActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private var isFirstLoaded = false
    private var delayedHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus && !isFirstLoaded) {
            isFirstLoaded = true

            delayedHandler = Handler(Looper.getMainLooper())
            delayedHandler?.let {
                HandlerCompat.postDelayed(it, {

                    // 创建一个意图，说明我要跳转到那个活动界面。
                    val intent = Intent(this, HomeActivity::class.java)
                    // 跳转到主要活动。
                    startActivity(intent)
                    // 再来个跳转过度动画。
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    // 销毁当前活动。
                    finish()

                }, null, 800L)
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        delayedHandler?.removeCallbacksAndMessages(null)

    }
}