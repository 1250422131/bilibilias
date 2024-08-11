package com.imcys.bilibilias.core.ffmpeg

object FFmpegUtil {

    /**
     * ffmpeg -y -i video.mp4 -i audio.m4a -vcodec copy -acodec copy output.mp4
     */
    fun mixAudioVideo(videoFile: String, audioFile: String, muxFile: String): Array<String> = buildCommandParams {
        append("-y")

        append("-i")
        append(videoFile)

        append("-i")
        append(audioFile)

        append("-vcodec")
        append("copy")
        append("-acodec")
        append("copy")

        append(muxFile)
    }
}
