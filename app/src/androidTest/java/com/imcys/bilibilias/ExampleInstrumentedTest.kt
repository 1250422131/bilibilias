package com.imcys.bilibilias

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.danmaku.change.DmXmlToAss
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    companion object {
        var TAG = "ExampleInstrumentedTest"
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.imcys.bilibilias", appContext.packageName)
    }

    @Test
    fun outputJson() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val videoIndex: String by lazy {
            appContext.getString(R.string.VideoIndex)
        }

        val bangumiEntry: String by lazy {
            appContext.getString(R.string.BangumiEntry)
        }

        Log.d(TAG, videoIndex)
        Log.d(TAG, bangumiEntry)
    }

}

