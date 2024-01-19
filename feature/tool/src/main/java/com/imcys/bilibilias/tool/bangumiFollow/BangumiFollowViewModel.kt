package com.imcys.bilibilias.tool.bangumiFollow

import android.content.Context
import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.imcys.biliAs.feature.tool.R
import com.imcys.bilibilias.tool.bangumiFollow.ExcelUtils.addCell
import com.imcys.common.utils.Result
import com.imcys.common.utils.updatePhotoMedias
import com.imcys.model.BangumiFollowList
import com.imcys.network.repository.BangumiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jxl.format.Alignment
import jxl.format.Border
import jxl.format.BorderLineStyle
import jxl.write.Label
import jxl.write.WritableCellFormat
import jxl.write.WritableFont
import jxl.write.WritableSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class BangumiFollowViewModel @Inject constructor(
    private val bangumiRepository: BangumiRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val selectableHeaders = mutableStateListOf<Pair<String, BangumiFollowHeaders>>()
    val selectedHeaders = mutableStateListOf<Pair<String, BangumiFollowHeaders>>()

    init {
        initHeaders(context)
    }

    private fun initHeaders(context: Context) {
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_title) to BangumiFollowHeaders.Title
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_ssid) to BangumiFollowHeaders.SeasonID
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_evaluate) to BangumiFollowHeaders.Evaluate
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_summart) to BangumiFollowHeaders.Summary
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_subtitle) to BangumiFollowHeaders.Subtitle
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_subtitle14) to BangumiFollowHeaders.Subtitle14
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_totalcount) to BangumiFollowHeaders.TotalCount
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_progress) to BangumiFollowHeaders.Progress
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_cover) to BangumiFollowHeaders.Cover
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_seasontitle) to BangumiFollowHeaders.SeasonTitle
        )
        selectableHeaders.add(
            context.getString(R.string.tool_log_export_bangumi_export_seasontypename) to BangumiFollowHeaders.SeasonTypeName
        )
    }

    private val bangumiList = mutableListOf<BangumiFollowList.Follow>()
    fun removeSelected(pair: Pair<String, BangumiFollowHeaders>) {
        val header = selectedHeaders.remove(pair)
        if (header) selectableHeaders.add(pair)
    }

    fun addToSelected(pair: Pair<String, BangumiFollowHeaders>) {
        val header = selectableHeaders.remove(pair)
        if (header) selectedHeaders.add(pair)
    }

    fun submit(context: Context) {
        getAllBangumiFollowList()
        createExcel(context)
    }

    private fun createExcel(context: Context) {
        Timber.d("createExcel=${bangumiList.size},${bangumiList.last()}")
        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "追番.xls"
        ).path

        Timber.d("path=$path")
        // 设置表头的字体大小和背景颜色
        val arial14font = WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD)
        arial14font.colour = jxl.format.Colour.LIGHT_BLUE
        val arial14format = WritableCellFormat(arial14font).apply {
            // 居中方式
            alignment = Alignment.CENTRE
            // 背景
            setBorder(Border.ALL, BorderLineStyle.THIN)
            setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW)
        }

        // 初始化一个excel表
        val workbook = ExcelUtils.initExcel(path)
        // 设置工作簿
        val sheet = workbook.createSheet("追番", 0)
        // 行高
        sheet.setRowView(0, 340)

        // 创建标题栏
        selectedHeaders.forEachIndexed { index, bangumiFollowLogHeaderBean ->
            sheet.addCell(Label(index, 0, bangumiFollowLogHeaderBean.first, arial14format))
            // 列宽
            sheet.setColumnView(index, 300)
        }
        editExcel(sheet)
        // 完成保存和写入
        workbook.write()
        workbook.close()
        updatePhotoMedias(context, File(path))
        Timber.d("createExcel=${bangumiList.size},${bangumiList.last()}")
    }

    private fun editExcel(sheet: WritableSheet) {
        val cellFormat = cellFormat()
        for ((row, follow) in bangumiList.withIndex()) {
            for ((column, header) in selectedHeaders.withIndex()) {
                when (header.second) {
                    BangumiFollowHeaders.Title -> sheet.addCell(
                        column,
                        row + 1,
                        follow.title,
                        cellFormat
                    )

                    BangumiFollowHeaders.Evaluate -> sheet.addCell(
                        column,
                        row + 1,
                        follow.evaluate,
                        cellFormat
                    )

                    BangumiFollowHeaders.SeasonID -> sheet.addCell(
                        column,
                        row + 1,
                        follow.seasonId.toString(),
                        cellFormat
                    )

                    BangumiFollowHeaders.Summary -> sheet.addCell(
                        column,
                        row + 1,
                        follow.summary,
                        cellFormat
                    )

                    BangumiFollowHeaders.Subtitle -> sheet.addCell(
                        column,
                        row + 1,
                        follow.subtitle,
                        cellFormat
                    )

                    BangumiFollowHeaders.Subtitle14 -> sheet.addCell(
                        column,
                        row + 1,
                        follow.subtitle14,
                        cellFormat
                    )

                    BangumiFollowHeaders.TotalCount -> sheet.addCell(
                        column,
                        row + 1,
                        follow.totalCount.toString(),
                        cellFormat
                    )

                    BangumiFollowHeaders.Progress -> sheet.addCell(
                        column,
                        row + 1,
                        follow.progress,
                        cellFormat
                    )

                    BangumiFollowHeaders.Cover -> sheet.addCell(
                        column,
                        row + 1,
                        follow.cover,
                        cellFormat
                    )

                    BangumiFollowHeaders.SeasonTitle -> sheet.addCell(
                        column,
                        row + 1,
                        follow.seasonTitle,
                        cellFormat
                    )

                    BangumiFollowHeaders.SeasonTypeName -> sheet.addCell(
                        column,
                        row + 1,
                        follow.seasonTypeName,
                        cellFormat
                    )
                }
            }
        }
    }

    private fun cellFormat(): WritableCellFormat {
        val arial14font = WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD)
        val arial10format = WritableCellFormat(arial14font)
        arial10format.alignment = Alignment.CENTRE
        return arial10format
    }

    fun getAllBangumiFollowList() {
        bangumiList.clear()
        var isBreak = false
        runBlocking {
            for (i in 1..Short.MAX_VALUE) {
                if (isBreak) return@runBlocking
                bangumiRepository.bangumiFollowList(i).collect { result ->
                    when (result) {
                        is Result.Error -> TODO()
                        Result.Loading -> {}
                        is Result.Success -> {
                            delay(1.seconds)
                            val list = result.data.list
                            if (list.isEmpty()) {
                                isBreak = true
                                return@collect
                            }
                            bangumiList.addAll(list)
                        }
                    }
                }
            }
        }
    }
}
