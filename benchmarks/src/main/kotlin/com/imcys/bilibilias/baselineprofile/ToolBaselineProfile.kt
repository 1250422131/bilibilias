package com.imcys.bilibilias.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.imcys.bilibilias.PACKAGE_NAME
import com.imcys.bilibilias.startActivityAndAllowNotifications
import com.imcys.bilibilias.tool.toolSelectTextFieldInputText
import org.junit.Rule
import org.junit.Test

class ToolBaselineProfile {
    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() =
        baselineProfileRule.collect(PACKAGE_NAME) {
            startActivityAndAllowNotifications()
            toolSelectTextFieldInputText()
        }
}
