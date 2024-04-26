package com.imcys.bilibilias.home.ui.activity.tool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityMergeVideoBinding
import com.imcys.bilibilias.home.ui.viewmodel.MargeVideoViewModel
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class MergeVideoActivity : BaseActivity() {
    lateinit var binding: ActivityMergeVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMergeVideoBinding?>(
            this,
            R.layout.activity_merge_video,
        ).apply {
            mergeVideoTopLy.addStatusBarTopPadding()
        }

        binding.margeVideoViewModel = MargeVideoViewModel()
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MergeVideoActivity::class.java)
            context.startActivity(intent)
        }
    }
}
