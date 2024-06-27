package com.imcys.bilibilias.home

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.untilHasChildren
import com.imcys.bilibilias.waitAndFindObject

fun MacrobenchmarkScope.forYouWaitForContent() {
    // Wait until content is loaded by checking if topics are loaded
//    device.wait(Until.gone(By.res("loadingWheel")), 5_000)
    // Sometimes, the loading wheel is gone, but the content is not loaded yet
    // So we'll wait here for topics to be sure
//    val obj = device.waitAndFindObject(By.res("forYou:topicSelection"), 10_000)
    // Timeout here is quite big, because sometimes data loading takes a long time!
//    obj.wait(untilHasChildren(), 60_000)
}