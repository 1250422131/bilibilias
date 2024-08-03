package com.imcys.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    LoadingScreen(
        modifier = modifier,
        content = {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.tertiary,
            )
//            val normal = painterResource(id = R.drawable)
//            val back = painterResource(id = R.drawable)
//            val fore = painterResource(id = R.drawable)
//            var painter by remember { mutableStateOf(normal) }
//            Image(painter = painter, contentDescription = "Loading")
//            LaunchedEffect(Unit) {
//                delay(80)
//                while (true) {
//                    painter = back
//                    delay(100)
//                    painter = normal
//                    delay(80)
//                    painter = fore
//                    delay(100)
//                    painter = normal
//                    delay(80)
//                }
//            }
        }
    )
}

@Composable
internal fun LoadingScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Surface(
        modifier = modifier,
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}