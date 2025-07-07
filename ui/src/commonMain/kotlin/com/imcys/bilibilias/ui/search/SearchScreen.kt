package com.imcys.bilibilias.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imcys.bilibilias.logic.search.SearchComponent

@Composable
fun SearchScreen(component: SearchComponent) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "SearchScreen")
        }
    }
}