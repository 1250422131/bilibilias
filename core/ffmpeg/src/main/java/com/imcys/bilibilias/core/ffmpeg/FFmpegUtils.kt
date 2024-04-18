package com.imcys.bilibilias.core.ffmpeg

object FFmpegUtils {
    /**
     * 使用ffmpeg命令行进行音视频合成
     *
     * @param videoFile 视频文件
     * @param audioFile 音频文件
     * @param duration  视频时长
     * @param muxFile   目标文件
     * @return 合成后的文件
     * command = "ffmpeg -y -i %s -i %s %s"
     */
    fun mixAudioVideo(videoFile: String, audioFile: String, muxFile: String): Array<String?> {
        return buildCommandParams {
            append("-i")
            append(videoFile)
            append("-i")
            append(audioFile)
            append(muxFile)
        }
    }
}
