package com.imcys.bilibilias.core.download.chore

import android.content.Context
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.ffmpeg.FFmpegWorker
import com.imcys.bilibilias.core.ffmpeg.IFFmpegWork
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultGroupTaskCall @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fFmpegWorker: FFmpegWorker,
    private val userPreferences: AsPreferencesDataSource,
    private val ifFmpegWork: IFFmpegWork,
) {
    fun execute(groupTask: GroupTask) {
//        if (groupTask.video.isCompeleted && groupTask.audio.isCompeleted) {
            try {
                getResponseWithInterceptorChain(groupTask)
            } finally {
            }
//        }
    }

    private fun getResponseWithInterceptorChain(groupTask: GroupTask) {
        val interceptors = mutableListOf<Interceptor<*>>()
        interceptors += MixingInterceptor(context, userPreferences, ifFmpegWork)
        interceptors += MoveFileInterceptor(context, userPreferences)
        val chain = Interceptor.Chain(interceptors, 0)
        return chain.proceed(groupTask)
    }
}
