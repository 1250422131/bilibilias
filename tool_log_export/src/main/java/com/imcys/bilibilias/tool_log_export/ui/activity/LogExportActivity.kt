package com.imcys.bilibilias.tool_log_export.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.drake.brv.annotaion.AnimationType
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.tool_log_export.R
import com.imcys.bilibilias.tool_log_export.base.activity.LogExportBaseActivity
import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
import com.imcys.bilibilias.tool_log_export.data.model.ExportItemBean
import com.imcys.bilibilias.tool_log_export.databinding.ActivityLogExportBinding
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

@RouterAnno(
    hostAndPath = ARouterAddress.LogExportActivity,
)
class LogExportActivity : LogExportBaseActivity() {

    lateinit var binding: ActivityLogExportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Component.inject(target = this)

        binding = DataBindingUtil.setContentView<ActivityLogExportBinding?>(
            this,
            R.layout.activity_log_export
        ).apply {
            logExportHomeTopLy.addStatusBarTopPadding()
        }

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


        binding.logExportHomeRv.linear().setup {
            //防抖动
            clickThrottle = 1000 // 单位毫秒
            setAnimation(AnimationType.SCALE)
            addType<ExportItemBean>(R.layout.log_export_item_export_tool)

            onClick(R.id.log_export_tool_item_ly) {
                activateClickEvent(getModel())
            }

        }.models = exportItemBeans


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
            Router
                .with(context)
                .hostAndPath(ARouterAddress.LogExportActivity)
                .forward { }
        }

    }
}