package com.imcys.bilibilias.feature.tool

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class SearchScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var clearSearchContentDesc: String
    private lateinit var clearRecentSearchesContentDesc: String
    private lateinit var tryAnotherSearchString: String
    private lateinit var searchNotReadyString: String
    private lateinit var searchResultNotFoundString: String

    @BeforeTest
    fun setup() {
        composeTestRule.activity.apply {
            clearSearchContentDesc = getString(R.string.feature_tool_clear_search_text_content_desc)
            clearRecentSearchesContentDesc = getString(R.string.feature_tool_clear_recent_searches_content_desc)
            tryAnotherSearchString = getString(R.string.feature_tool_try_another_search)
            searchNotReadyString = getString(R.string.feature_tool_not_ready)
            searchResultNotFoundString = getString(R.string.feature_tool_result_not_found)
        }
    }

    @Test
    fun searchTextField_isFocused() {
        composeTestRule.setContent {
            SearchScreen()
        }

        composeTestRule
            .onNodeWithTag("searchTextField")
            .assertIsFocused()
    }

    @Composable
    fun SearchScreen(
        searchQuery: String = "",
        searchResultUiState: SearchResultUiState = SearchResultUiState.EmptyQuery,
    ) {
        ToolContent(
            searchQuery = searchQuery,
            searchResultUiState = searchResultUiState,
            onSearchQueryChanged = {},
            onDownload = {},
            navigationToSettings = {},
            navigationToAuthorSpace = {},
        )
    }
}
