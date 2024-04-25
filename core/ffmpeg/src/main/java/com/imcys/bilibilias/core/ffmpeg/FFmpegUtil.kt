package com.imcys.bilibilias.core.ffmpeg

object FFmpegUtil {
    /**
     * 使用ffmpeg命令行进行音视频合成
     *
     * @param videoFile 视频文件
     * @param audioFile 音频文件
     * @param duration  视频时长
     * @param muxFile   目标文件
     * @return 合成后的文件
     * ffmpeg -i video.mp4 -i audio.m4a -c:v copy -c:a copy output.mp4
     */
    fun mixAudioVideo(videoFile: String, audioFile: String, muxFile: String): Array<String?> {
        return buildCommandParams {
            append("-i")
            append(videoFile)
            append("-i")
            append(audioFile)
            append("-c:v")
            append("copy")
            append("-c:a")
            append("copy")
            append(muxFile)
        }
    }
}
