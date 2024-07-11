package com.liulishuo.okdownload.di

import android.content.Context
import dagger.hilt.InstallIn
import dagger.hilt.android.EarlyEntryPoint
import dagger.hilt.android.EarlyEntryPoints
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.ExecutorService

@EarlyEntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {
    fun injectOkhttp(): OkHttpClient
    fun injectExecutorService(): ExecutorService

    companion object {
        fun resolve(context: Context): InitializerEntryPoint {
            return EarlyEntryPoints.get(context, InitializerEntryPoint::class.java)
        }
    }
}
