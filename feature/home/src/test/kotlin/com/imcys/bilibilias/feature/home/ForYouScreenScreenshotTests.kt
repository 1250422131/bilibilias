package com.imcys.bilibilias.feature.home

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.imcys.bilibilias.core.designsystem.component.AsBackground
import com.imcys.bilibilias.core.designsystem.theme.AsTheme
import com.imcys.bilibilias.core.testing.util.DefaultTestDevices
import com.imcys.bilibilias.core.testing.util.captureForDevice
import com.imcys.bilibilias.core.testing.util.captureMultiDevice
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import kotlin.test.Test

/**
 * Screenshot tests for the [ForYouScreen].
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ForYouScreenScreenshotTests {

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setTimeZone() {
    }

    @Test
    fun forYouScreenPopulatedFeed() {
        composeTestRule.captureMultiDevice("ForYouScreenPopulatedFeed") {
            AsTheme {
//                ForYouScreen(
//                    isSyncing = false,
//                    onboardingUiState = NotShown,
//                    feedState = Success(
//                        feed = userNewsResources,
//                    ),
//                    onTopicCheckedChanged = { _, _ -> },
//                    saveFollowedTopics = {},
//                    onNewsResourcesCheckedChanged = { _, _ -> },
//                    onNewsResourceViewed = {},
//                    onTopicClick = {},
//                    deepLinkedUserNewsResource = null,
//                    onDeepLinkOpened = {},
//                )
            }
        }
    }

    @Test
    fun forYouScreenLoading() {
        composeTestRule.captureMultiDevice("ForYouScreenLoading") {
            AsTheme {
//                ForYouScreen(
//                    isSyncing = false,
//                    onboardingUiState = Loading,
//                    feedState = NewsFeedUiState.Loading,
//                    onTopicCheckedChanged = { _, _ -> },
//                    saveFollowedTopics = {},
//                    onNewsResourcesCheckedChanged = { _, _ -> },
//                    onNewsResourceViewed = {},
//                    onTopicClick = {},
//                    deepLinkedUserNewsResource = null,
//                    onDeepLinkOpened = {},
//                )
            }
        }
    }

    @Test
    fun forYouScreenTopicSelection() {
        composeTestRule.captureMultiDevice("ForYouScreenTopicSelection") {
            ForYouScreenTopicSelection()
        }
    }

    @Test
    fun forYouScreenTopicSelection_dark() {
        composeTestRule.captureForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "ForYouScreenTopicSelection",
            darkMode = true,
        ) {
            ForYouScreenTopicSelection()
        }
    }

    @Test
    fun forYouScreenPopulatedAndLoading() {
        composeTestRule.captureMultiDevice("ForYouScreenPopulatedAndLoading") {
            ForYouScreenPopulatedAndLoading()
        }
    }

    @Test
    fun forYouScreenPopulatedAndLoading_dark() {
        composeTestRule.captureForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "ForYouScreenPopulatedAndLoading",
            darkMode = true,
        ) {
            ForYouScreenPopulatedAndLoading()
        }
    }

    @Composable
    private fun ForYouScreenTopicSelection() {
        AsTheme {
            AsBackground {
//                ForYouScreen(
//                    isSyncing = false,
//                    onboardingUiState = Shown(
//                        topics = userNewsResources.flatMap { news -> news.followableTopics }
//                            .distinctBy { it.topic.id },
//                    ),
//                    feedState = Success(
//                        feed = userNewsResources,
//                    ),
//                    onTopicCheckedChanged = { _, _ -> },
//                    saveFollowedTopics = {},
//                    onNewsResourcesCheckedChanged = { _, _ -> },
//                    onNewsResourceViewed = {},
//                    onTopicClick = {},
//                    deepLinkedUserNewsResource = null,
//                    onDeepLinkOpened = {},
//                )
            }
        }
    }

    @Composable
    private fun ForYouScreenPopulatedAndLoading() {
        AsTheme {
            AsBackground {
                AsTheme {
//                    ForYouScreen(
//                        isSyncing = true,
//                        onboardingUiState = Loading,
//                        feedState = Success(
//                            feed = userNewsResources,
//                        ),
//                        onTopicCheckedChanged = { _, _ -> },
//                        saveFollowedTopics = {},
//                        onNewsResourcesCheckedChanged = { _, _ -> },
//                        onNewsResourceViewed = {},
//                        onTopicClick = {},
//                        deepLinkedUserNewsResource = null,
//                        onDeepLinkOpened = {},
//                    )
                }
            }
        }
    }
}