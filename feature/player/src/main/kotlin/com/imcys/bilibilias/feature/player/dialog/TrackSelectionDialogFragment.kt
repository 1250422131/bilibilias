package com.imcys.bilibilias.feature.player.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.media3.common.C
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imcys.bilibilias.feature.player.R
import com.imcys.bilibilias.feature.player.extensions.getName

@UnstableApi
class TrackSelectionDialogFragment(
    private val type: @C.TrackType Int,
    private val tracks: Tracks,
    private val onTrackSelected: (trackIndex: Int) -> Unit,
    private val onOpenLocalTrackClicked: () -> Unit = {},
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = when (type) {
        C.TRACK_TYPE_AUDIO -> {
            val audioTracks = tracks.groups
                .filter { it.type == C.TRACK_TYPE_AUDIO && it.isSupported }

            val trackNames = audioTracks.mapIndexed { index, trackGroup ->
                trackGroup.mediaTrackGroup.getName(type, index)
            }.toTypedArray()

            val selectedTrackIndex = audioTracks
                .indexOfFirst { it.isSelected }.takeIf { it != -1 } ?: audioTracks.size

            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.select_audio_track))
            if (trackNames.isNotEmpty()) {
                builder.setSingleChoiceItems(
                    arrayOf(*trackNames, getString(R.string.disable)),
                    selectedTrackIndex,
                ) { dialog, trackIndex ->
                    onTrackSelected(trackIndex.takeIf { it < trackNames.size } ?: -1)
                    dialog.dismiss()
                }
            } else {
                builder.setMessage(getString(R.string.no_audio_tracks_found))
            }
            builder.create()
        }

        C.TRACK_TYPE_TEXT -> {
            val textTracks = tracks.groups
                .filter { it.type == C.TRACK_TYPE_TEXT && it.isSupported }

            val trackNames = textTracks.mapIndexed { index, trackGroup ->
                trackGroup.mediaTrackGroup.getName(type, index)
            }.toTypedArray()

            val selectedTrackIndex = textTracks
                .indexOfFirst { it.isSelected }.takeIf { it != -1 } ?: textTracks.size

            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.select_subtitle_track))
                .setPositiveButton(getString(R.string.open_subtitle)) { dialog, _ ->
                    dialog.dismiss()
                    onOpenLocalTrackClicked()
                }
            if (trackNames.isNotEmpty()) {
                builder.setSingleChoiceItems(
                    arrayOf(*trackNames, getString(R.string.disable)),
                    selectedTrackIndex,
                ) { dialog, trackIndex ->
                    onTrackSelected(trackIndex.takeIf { it < trackNames.size } ?: -1)
                    dialog.dismiss()
                }
            } else {
                builder.setMessage(getString(R.string.no_subtitle_tracks_found))
            }
            builder.create()
        }

        else -> throw IllegalArgumentException(
            "Track type not supported. Track type must be either TRACK_TYPE_AUDIO or TRACK_TYPE_TEXT",
        )
    }
}
