package com.imcys.bilibilias.home.ui.activity.tool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.view.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityMergeVideoBinding
import com.imcys.bilibilias.home.ui.viewmodel.MargeVideoViewModel
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class MergeVideoActivity : BaseActivity<ActivityMergeVideoBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_merge_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mergeVideoTopLy.addStatusBarTopPadding()

        binding.margeVideoViewModel = MargeVideoViewModel()
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MergeVideoActivity::class.java)
            context.startActivity(intent)
        }
    }
}
