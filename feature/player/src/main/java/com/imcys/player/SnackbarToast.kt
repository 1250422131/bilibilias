package com.imcys.player

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*

@Composable
fun ShowSnackbar(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    Box {
        val hostState = SnackbarHostState()
        SnackbarHost(hostState = hostState, modifier = Modifier.align(Alignment.Center))
        LaunchedEffect(Unit) {
            hostState.showSnackbar(message, actionLabel, withDismissAction, duration)
        }
    }
}

@Composable
fun Snackbar(message: String, showText: Boolean = false) {
    Box {
        val hostState = SnackbarHostState()
        SnackbarHost(hostState = hostState, modifier = Modifier.align(Alignment.Center)) {
            Card(
                Modifier
                    .defaultMinSize(minWidth = 96.dp, minHeight = 96.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = it.visuals.message,
                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        )
                        .scale(2.0f)
                        .padding(top = 16.dp)
                )
                if (showText) {
                    Text(
                        text = it.visuals.message,
                        modifier = Modifier
                            .align(
                                Alignment.CenterHorizontally
                            )
                            .padding(bottom = 16.dp, top = 16.dp)
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            hostState.showSnackbar(message = message, duration = SnackbarDuration.Long)
        }
    }
}

@Composable
fun SnackbarCircularProgress(message: String) {
    Box {
        val hostState = SnackbarHostState()

        SnackbarHost(hostState = hostState, modifier = Modifier.align(Alignment.Center)) {
            Card(
                Modifier
                    .size(96.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                var isStop by remember { mutableStateOf(false) }
                val progress by animateFloatAsState(
                    targetValue = if (isStop) 1f else 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "progress"
                )

                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .size(50.dp)
                            .align(
                                Alignment.Center
                            )
                            .rotate(360 * progress),
                        color = Color.Yellow,
                        strokeWidth = 4.dp,
                        trackColor = Color.Transparent,
                        strokeCap = StrokeCap.Round,
                    )
//                    Icon(
//                        imageVector = Icons.Default.Refresh,
//                        contentDescription = "progress",
//                        modifier = Modifier.scale(2.0f).rotate(360 * progress.value).align(Alignment.Center)
//                    )
                }
                LaunchedEffect(Unit) {
                    isStop = true
                }
            }
        }
        LaunchedEffect(Unit) {
            hostState.showSnackbar(message = message, duration = SnackbarDuration.Indefinite)
            delay(10000L)
            hostState.currentSnackbarData?.dismiss()
        }
    }
}
