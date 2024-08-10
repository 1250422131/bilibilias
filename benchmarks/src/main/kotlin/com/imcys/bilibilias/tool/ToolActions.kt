package com.imcys.bilibilias.tool

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By

fun MacrobenchmarkScope.toolSelectTextFieldInputText() {
    val textField = device.findObject(By.res("searchTextField"))
    textField.text = "BV16S411w7gZ"
}
