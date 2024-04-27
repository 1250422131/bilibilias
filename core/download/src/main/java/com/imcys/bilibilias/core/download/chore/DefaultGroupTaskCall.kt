package com.imcys.bilibilias.core.download.chore

import android.content.Context
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.ffmpeg.FFmpegWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultGroupTaskCall @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fFmpegWorker: FFmpegWorker,
) {
    fun execute(groupTask: GroupTask) {
//        OkHttpClient.Builder().addInterceptor()
        if (groupTask.video.isCompelete && groupTask.audio.isCompelete) {
            try {
                getResponseWithInterceptorChain(groupTask)
            } finally {
            }
        }
    }

    private fun getResponseWithInterceptorChain(groupTask: GroupTask) {
        val interceptors = mutableListOf<Interceptor<*>>()
        interceptors += MixingInterceptor(context, fFmpegWorker)
        interceptors += MoveFileInterceptor(context)
        val chain = Interceptor.Chain(interceptors,0)
        return chain.proceed(groupTask)
    }
}
