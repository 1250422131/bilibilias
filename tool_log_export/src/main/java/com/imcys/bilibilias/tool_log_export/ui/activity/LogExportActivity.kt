package com.imcys.bilibilias.tool_log_export.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.tool_log_export.R
import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
import com.imcys.bilibilias.tool_log_export.data.model.ExportItemBean
import com.imcys.bilibilias.tool_log_export.databinding.ActivityLogExportBinding

class LogExportActivity : AppCompatActivity() {

    lateinit var binding: ActivityLogExportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityLogExportBinding?>(
            this,
            R.layout.activity_log_export
        )

        initView()
    }

    private fun initView() {
        loadRvData()
    }

    private fun loadRvData() {
        val exportItemBeans = mutableListOf(
            ExportItemBean(
                ExportItemEnum.BangumiFollowLog,
                getString(R.string.tool_activity_log_export_tool_item_Trace_up_historical_export_title),
                getString(R.string.tool_activity_log_export_tool_item_Trace_up_historical_export_long_title),
                "",
                "https://s1.ax1x.com/2023/02/06/pS6OIfg.png"
            )
        )
    }

    private fun activateClickEvent(model: ExportItemBean) {
        when (model.itemType) {
            ExportItemEnum.BangumiFollowLog -> {
                BangumiFollowLogActivity.actionStart(this)
            }

            ExportItemEnum.VideoLog -> {
            }
        }
    }

    companion object {
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, LogExportActivity::class.java))
        }
    }
}
