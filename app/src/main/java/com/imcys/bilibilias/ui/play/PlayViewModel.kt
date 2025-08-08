package com.imcys.bilibilias.ui.play

import android.content.Context
import android.view.Surface
import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.render.NativeVideoRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayViewModel : ViewModel() {

    private val renderer = NativeVideoRenderer()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized = _isInitialized.asStateFlow()

    fun initializeRenderer(surface: Surface, videoFileFd: Int, width: Int, height: Int) {
        if (renderer.init(surface, videoFileFd)) {
            renderer.setViewport(width, height)
            _isInitialized.value = true
        }
    }

    fun updateViewport(width: Int, height: Int) {
        renderer.setViewport(width, height)
    }

    fun play() {
        _isPlaying.value = true
        // TODO
    }

    fun pause() {
        _isPlaying.value = false
        // TODO
    }

    fun release() {
        renderer.release()
    }

    override fun onCleared() {
        super.onCleared()
        renderer.release()
    }

}
