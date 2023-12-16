package com.imcys.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun EmptyScreen(
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var count by remember { mutableIntStateOf(0) }
//        Image(
//            painter = painterResource(id = R.drawable),
//            contentDescription = "Logo",
//            modifier = Modifier.clickable(
//                remember { MutableInteractionSource() }, null, true, null, null
//            ) {
//                if (count++ > 17) {
//                    scope.launch {  }
//                }
//            }
//        )
        content()
    }
}