package com.imcys.bilibilias.home.ui.activity.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.databinding.ActivityUserVideoDownloadBinding
import com.imcys.bilibilias.home.ui.viewmodel.UserVideoDownloadViewModel
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserVideoDownloadActivity : BaseActivity<ActivityUserVideoDownloadBinding>() {
    override val layoutId = R.layout.activity_user_video_download

    private val viewModel: UserVideoDownloadViewModel by viewModels<UserVideoDownloadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initUserList()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        binding.apply {
            uvDownloadTopLy.addStatusBarTopPadding()
        }
    }
    private fun initUserList() {
        val mid = intent.getLongExtra("mid", BaseApplication.asUser.mid)
        viewModel.loadUserAllVideo(mid.toString())
    }

    companion object {
        fun actionStart(context: Context, mid: Long?) {
            val intent = Intent(context, UserVideoDownloadActivity::class.java)
            intent.putExtra("mid", mid)
            context.startActivity(intent)
        }
    }
}
