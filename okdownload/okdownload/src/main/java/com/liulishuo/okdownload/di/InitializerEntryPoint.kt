package com.liulishuo.okdownload.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.ExecutorService

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {
    fun injectOkhttp(): OkHttpClient
    fun injectExecutorService(): ExecutorService

    companion object {
        fun resolve(context: Context): InitializerEntryPoint {
            return EntryPointAccessors.fromApplication(
                context,
                InitializerEntryPoint::class.java
            )
        }
    }
}
