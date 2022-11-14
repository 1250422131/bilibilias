package com.imcys.bilibilias.home.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.model.view.VideoBaseBean
import com.imcys.bilibilias.utils.HttpUtils

class AsVideoActivity : BaseActivity() {

    lateinit var binding: ActivityAsVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_as_video)
        statusBarOnly(this)


        initVideoData()

    }

    private fun initVideoData() {

        val intent = intent
        val bvId = intent.getStringExtra("bvId")

        HttpUtils.get(BilibiliApi.getVideoDataPath + "?bvid=$bvId", VideoBaseBean::class.java) {
            binding.videoBaseBean = it
        }
    }
}