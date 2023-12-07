package com.imcys.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CenterRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically, content)
}

@Composable
fun CenterColumn(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier, Arrangement.Center, Alignment.CenterHorizontally, content)
}

@Composable
fun VerticalTwoTerms(top: @Composable () -> Unit, bottom: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier, Arrangement.Center, Alignment.CenterHorizontally) {
        top()
        bottom()
    }
}
