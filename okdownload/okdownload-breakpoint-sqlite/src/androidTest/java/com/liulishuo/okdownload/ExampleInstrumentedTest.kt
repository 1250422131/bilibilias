<<<<<<<< HEAD:okdownload/okdownload-breakpoint-sqlite/src/androidTest/java/com/liulishuo/okdownload/ExampleInstrumentedTest.kt
package com.liulishuo.okdownload
========
package com.imcys.bilibilias.okdownloader
>>>>>>>> c7819abd2aac55602832c43746a91a99d3f1b832:core/okdownloader/src/androidTest/java/com/imcys/bilibilias/okdownloader/ExampleInstrumentedTest.kt

import androidx.test.platform.app.InstrumentationRegistry
<<<<<<<< HEAD:okdownload/okdownload-breakpoint-sqlite/src/androidTest/java/com/liulishuo/okdownload/ExampleInstrumentedTest.kt
import org.junit.Assert.assertEquals
========
import androidx.test.ext.junit.runners.AndroidJUnit4

>>>>>>>> c7819abd2aac55602832c43746a91a99d3f1b832:core/okdownloader/src/androidTest/java/com/imcys/bilibilias/okdownloader/ExampleInstrumentedTest.kt
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
<<<<<<<< HEAD:okdownload/okdownload-breakpoint-sqlite/src/androidTest/java/com/liulishuo/okdownload/ExampleInstrumentedTest.kt
        assertEquals("com.liulishuo.okdownload.test", appContext.packageName)
========
        assertEquals("com.imcys.bilibilias.okdownload.test", appContext.packageName)
>>>>>>>> c7819abd2aac55602832c43746a91a99d3f1b832:core/okdownloader/src/androidTest/java/com/imcys/bilibilias/okdownloader/ExampleInstrumentedTest.kt
    }
}
