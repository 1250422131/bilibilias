package com.imcys.player.sheet

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.imcys.designsystem.theme.BILIBILIASTheme
import com.imcys.model.video.Owner
import com.imcys.model.video.ToolBarReport
import com.imcys.player.state.PlayInfoUiState
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class SheetSeriesTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun sheetSeries() {
        composeTestRule.setContent {
            BILIBILIASTheme {
                val qualityDesc = persistentListOf(
                    "真彩 HDR" to 125,
                    "超清 4K" to 120,
                    "高清 1080P60" to 116,
                    "高清 1080P" to 80,
                    "高清 720P60" to 64,
                    "清晰 480P" to 32,
                    "流畅 360P" to 16,
                )
                SheetSeries(
                    qualityDesc,
                    { _, _ -> },
                    PlayInfoUiState.Success(
                        1, "2", 3, "这是一个标题", "", "", Owner(),
                        ToolBarReport(), persistentListOf()
                    )
                )
            }
        }
        composeTestRule
            .onNode(hasText("这是一个标题") and hasClickAction())
            .assertExists()
        composeTestRule.onNodeWithText("全部下载").assertHasClickAction()
    }
}
