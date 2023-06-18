package com.imcys.bilibilias.tool_log_export.utils

import jxl.Workbook
import jxl.format.CellFormat
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import java.io.File


object ExcelUtils {

    fun initExcel(path: String): WritableWorkbook {

        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }

        return Workbook.createWorkbook(file)
    }


    fun WritableSheet.addCell(
        column: Int,
        row: Int,
        content: String,
        st: CellFormat
    ): WritableSheet {
        this.addCell(Label(column, row, content, st))
        return this
    }


}