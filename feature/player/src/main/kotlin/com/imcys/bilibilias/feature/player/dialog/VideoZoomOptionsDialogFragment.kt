package com.imcys.bilibilias.feature.player.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.media3.common.util.UnstableApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imcys.bilibilias.feature.player.R
import com.imcys.bilibilias.feature.player.model.VideoZoom

@UnstableApi
class VideoZoomOptionsDialogFragment(
    private val currentVideoZoom: VideoZoom,
    private val onVideoZoomOptionSelected: (videoZoom: VideoZoom) -> Unit,
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val videoZoomValues = VideoZoom.entries.toTypedArray()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.feature_player_video_zoom))
            .setSingleChoiceItems(
                videoZoomValues.map { getString(it.nameRes()) }.toTypedArray(),
                videoZoomValues.indexOfFirst { it == currentVideoZoom },
            ) { dialog, trackIndex ->
                onVideoZoomOptionSelected(videoZoomValues[trackIndex])
                dialog.dismiss()
            }.create()
    }
}

fun VideoZoom.nameRes(): Int {
    val stringRes = when (this) {
        VideoZoom.BEST_FIT -> R.string.feature_player_best_fit
        VideoZoom.STRETCH -> R.string.feature_player_stretch
        VideoZoom.CROP -> R.string.feature_player_crop
        VideoZoom.HUNDRED_PERCENT -> R.string.feature_player_hundred_percent
    }

    return stringRes
}
