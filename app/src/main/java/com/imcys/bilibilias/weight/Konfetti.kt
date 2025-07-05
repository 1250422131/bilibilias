package com.imcys.bilibilias.weight

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.ColorUtils
import androidx.core.view.HapticFeedbackConstantsCompat
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

fun Int.blend(
    color: Int,
    @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.5f,
): Int = ColorUtils.blendARGB(this, color, fraction)


@Composable
fun rememberKonfettiState(visible: Boolean = false): MutableState<Boolean> {
    return remember { mutableStateOf(visible) }
}

@Composable
fun Konfetti(
    state: MutableState<Boolean> = rememberKonfettiState(),
    modifier: Modifier = Modifier,
    primary: Color = MaterialTheme.colorScheme.primary
) {
    var visible by state
    if (!visible) {
        return
    }
    val view = LocalView.current

    val listener = remember(state) {
        object : OnParticleSystemUpdateListener {
            override fun onParticleSystemEnded(system: PartySystem, activeSystems: Int) {
                if (activeSystems == 0) {
                    visible = false
                } else {
                    view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
                }
            }
        }
    }
    KonfettiView(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        parties = remember { particles(primary.toArgb()) },
        updateListener = listener
    )
}

private val defaultColors = listOf(
    0xfce18a,
    0x009688,
    0xff726d,
    0xf4306d,
    0xb48def,
    0x95FF82,
    0x82ECFF,
    0xFF9800,
    0x0E008A,
)
private const val colorBlendFraction = 0.3f

private fun particles(primary: Int) = listOf(
    Party(
        speed = 0f,
        maxSpeed = 12f,
        damping = 0.9f,
        angle = Angle.BOTTOM,
        spread = Spread.ROUND,
        colors = defaultColors.map { it.blend(primary, colorBlendFraction) },
        emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100),
        position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0)),
    ),
    Party(
        speed = 10f,
        maxSpeed = 30f,
        damping = 0.9f,
        angle = Angle.RIGHT - 55,
        spread = 60,
        colors = defaultColors.map { it.blend(primary, colorBlendFraction) },
        emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100),
        position = Position.Relative(0.0, 1.0)
    ),
    Party(
        speed = 10f,
        maxSpeed = 30f,
        damping = 0.9f,
        angle = Angle.RIGHT - 125,
        spread = 60,
        colors = defaultColors.map { it.blend(primary, colorBlendFraction) },
        emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100),
        position = Position.Relative(1.0, 1.0)
    )
)
