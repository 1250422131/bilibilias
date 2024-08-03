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
import kotlinx.collections.immutable.persistentMapOf
import org.junit.Rule
import org.junit.Test

class SheetSeriesTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun sheetSeries() {
        composeTestRule.setContent {
            BILIBILIASTheme {
                val qualityDesc = persistentMapOf(
                    125 to "真彩 HDR",
                    120 to "超清 4K",
                    116 to "高清 1080P60",
                    80 to "高清 1080P",
                    64 to "高清 720P60",
                    32 to "清晰 480P",
                    16 to "流畅 360P",
                )
                SheetSeries(
                    qualityDesc,
                    { _, _ -> },
                    PlayInfoUiState.Success(
                        1, "2", 3, "这是一个标题", "", "", Owner(),
                        ToolBarReport(), persistentListOf()
                    ),
                    0
                )
            }
        }
        composeTestRule
            .onNode(hasText("这是一个标题") and hasClickAction())
            .assertExists()
        composeTestRule.onNodeWithText("全部下载").assertHasClickAction()
    }
}
