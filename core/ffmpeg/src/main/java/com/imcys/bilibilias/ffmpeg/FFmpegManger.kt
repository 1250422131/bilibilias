package com.imcys.bilibilias.ffmpeg

class FFmpegManger {

    /**
     * A native method that is implemented by the 'ffmpeg' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun getFFmpegVersion():String

    companion object {
        // Used to load the 'ffmpeg' library on application startup.
        init {
            System.loadLibrary("ffmpeg")
        }
    }
}