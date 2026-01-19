package com.imcys.bilibilias.ui.analysis

import com.imcys.bilibilias.database.entity.download.MediaContainer

sealed interface AnalysisIntent {
    data class UpdateAudioContainer(val mediaContainer: MediaContainer) : AnalysisIntent
    data class UpdateVideoContainer(val mediaContainer: MediaContainer) : AnalysisIntent

}