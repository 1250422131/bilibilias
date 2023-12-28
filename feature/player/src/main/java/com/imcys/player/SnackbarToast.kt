package com.imcys.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

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
                        progress = progress,
                        strokeWidth = 4.dp,
                        strokeCap = StrokeCap.Round,
                        trackColor = Color.Transparent,
                        color = Color.Yellow,
                        modifier = Modifier
                            .size(50.dp)
                            .align(
                                Alignment.Center
                            )
                            .rotate(360 * progress)
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
