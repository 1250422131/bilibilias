package com.imcys.bilibilias.di

import android.app.Activity
import android.view.Window
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.JankStats.OnFrameListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.aakira.napier.Napier

@Module
@InstallIn(ActivityComponent::class)
class JankStatsModule {
    @Provides
    fun providesOnFrameListener(): JankStats.OnFrameListener = OnFrameListener { frameData ->
        // Make sure to only log janky frames.
        if (frameData.isJank) {
            // We're currently logging this but would better report it to a backend.
            Napier.v(frameData.toString(), tag = "As Jank",)
        }
    }

    @Provides
    fun providesWindow(activity: Activity): Window = activity.window

    @Provides
    fun providesJankStats(
        window: Window,
        frameListener: OnFrameListener,
    ): JankStats = JankStats.createAndTrack(window, frameListener)
}
