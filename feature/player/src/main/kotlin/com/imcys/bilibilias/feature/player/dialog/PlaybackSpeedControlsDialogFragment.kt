package com.imcys.bilibilias.feature.player.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.imcys.bilibilias.feature.player.R
import com.imcys.bilibilias.feature.player.util.round

class PlaybackSpeedControlsDialogFragment(
    private val currentSpeed: Float,
    private val skipSilenceEnabled: Boolean,
    private val onChange: (Float) -> Unit,
    private val onSkipSilenceChanged: (Boolean) -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity ?: error("Activity cannot be null")
        val view = activity.layoutInflater.inflate(R.layout.feature_player_playback_speed, null)

        val speedText: TextView = view.findViewById(R.id.speed_text)
        val decSpeed: Button = view.findViewById(R.id.dec_speed)
        val incSpeed: Button = view.findViewById(R.id.inc_speed)
        val resetSpeed: Button = view.findViewById(R.id.reset_speed)
        val speed: Slider = view.findViewById(R.id.speed)

        val button02x: Button = view.findViewById(R.id.button_0_2x)
        val button05x: Button = view.findViewById(R.id.button_0_5x)
        val button10x: Button = view.findViewById(R.id.button_1_0x)
        val button15x: Button = view.findViewById(R.id.button_1_5x)
        val button20x: Button = view.findViewById(R.id.button_2_0x)
        val button25x: Button = view.findViewById(R.id.button_2_5x)
        val button30x: Button = view.findViewById(R.id.button_3_0x)
        val button35x: Button = view.findViewById(R.id.button_3_5x)
        val button40x: Button = view.findViewById(R.id.button_4_0x)
        val skipSilence: SwitchCompat = view.findViewById(R.id.skip_silence)

        speedText.text = currentSpeed.toString()
        speed.value = currentSpeed.round(1)
        skipSilence.isChecked = skipSilenceEnabled

        speed.addOnChangeListener { _, _, _ ->
            val newSpeed = speed.value.round(1)
            onChange(newSpeed)
            speedText.text = newSpeed.toString()
        }
        incSpeed.setOnClickListener {
            if (speed.value < 4.0f) {
                speed.value = (speed.value + 0.1f).round(1)
            }
        }
        decSpeed.setOnClickListener {
            if (speed.value > 0.2f) {
                speed.value = (speed.value - 0.1f).round(1)
            }
        }
        resetSpeed.setOnClickListener { speed.value = 1.0f }
        button02x.setOnClickListener { speed.value = 0.2f }
        button05x.setOnClickListener { speed.value = 0.5f }
        button10x.setOnClickListener { speed.value = 1.0f }
        button15x.setOnClickListener { speed.value = 1.5f }
        button20x.setOnClickListener { speed.value = 2.0f }
        button25x.setOnClickListener { speed.value = 2.5f }
        button30x.setOnClickListener { speed.value = 3.0f }
        button35x.setOnClickListener { speed.value = 3.5f }
        button40x.setOnClickListener { speed.value = 4.0f }

        skipSilence.setOnCheckedChangeListener { _, isChecked ->
            onSkipSilenceChanged(isChecked)
        }

        val builder = MaterialAlertDialogBuilder(activity)
        return builder.setTitle(getString(R.string.select_playback_speed))
            .setView(view)
            .create()
    }
}
