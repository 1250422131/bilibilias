package com.imcys.bilibilias.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.core.designsystem.reveal.circularReveal
import com.imcys.bilibilias.core.designsystem.theme.AsTheme
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
//        DialogUtils.dialog(
//            this,
//            getString(R.string.app_permission_application_title),
//            content,
//            getString(R.string.app_permission_application_confirm),
//            getString(R.string.app_permission_application_cancel),
//            false,
//            positiveButtonClickListener = {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
//                )
//            },
//            negativeButtonClickListener = {
//                finish()
//            },
//        ).show()
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

@Composable
fun DDDD() {
    val configuration = LocalConfiguration.current

    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible.value = !isVisible.value
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = (configuration.screenHeightDp * .3).dp)
            .circularReveal(
                isVisible.value,
                durationMillis = 1000,
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .width(120.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.clipToBounds(),
            fontSize = 56.sp,
            color = MaterialTheme.colorScheme.primary,
            softWrap = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PPPP() {
    AsTheme {
        DDDD()
    }
}
