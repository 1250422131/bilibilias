package com.imcys.bilibilias.core.designsystem.component

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.testing.util.captureMultiTheme
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class BackgroundScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun asBackground_multipleThemes() {
        composeTestRule.captureMultiTheme("Background") { description ->
            AsBackground(Modifier.size(100.dp)) {
                Text("$description background")
            }
        }
    }

    @Test
    fun asGradientBackground_multipleThemes() {
        composeTestRule.captureMultiTheme("Background", "GradientBackground") { description ->
            AsGradientBackground(Modifier.size(100.dp)) {
                Text("$description background")
            }
        }
    }
}
