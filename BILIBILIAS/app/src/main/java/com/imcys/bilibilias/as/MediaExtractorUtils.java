package com.imcys.bilibilias.as;

import java.io.File;

import android.os.Build;
import android.os.Environment;

import java.io.IOException;

import android.media.MediaMuxer;
import android.media.MediaFormat;
import android.media.MediaExtractor;

import java.nio.ByteBuffer;

import android.media.MediaCodec;

import androidx.annotation.RequiresApi;

import java.io.File;

import android.os.Environment;

import java.io.IOException;

import android.media.MediaMuxer;
import android.media.MediaFormat;
import android.media.MediaExtractor;

import java.nio.ByteBuffer;

import android.media.MediaCodec;

import java.io.File;

import android.os.Environment;

import java.io.IOException;

import android.media.MediaMuxer;
import android.media.MediaFormat;
import android.media.MediaExtractor;

import java.nio.ByteBuffer;

import android.media.MediaCodec;

public class MediaExtractorUtils {

    /**
     * @param videoPath  源视频路径
     * @param audioPath  源音频路径
     * @param outPath    输出路径
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void startComposeTrack(String videoPath, String audioPath, String outPath) {
        try {
            MediaExtractor videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(videoPath);
            MediaExtractor audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(audioPath);
            MediaMuxer muxer = new MediaMuxer(outPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            videoExtractor.selectTrack(0);
            MediaFormat videoFormat = videoExtractor.getTrackFormat(0);
            int videoTrack = muxer.addTrack(videoFormat);
            audioExtractor.selectTrack(0);
            MediaFormat audioFormat = audioExtractor.getTrackFormat(0);
            int audioTrack = muxer.addTrack(audioFormat);

            boolean sawEOS = false;
            int frameCount = 0;
            int offset = 100;
            int sampleSize = 256 * 1024;
            ByteBuffer videoBuf = ByteBuffer.allocate(sampleSize);
            ByteBuffer audioBuf = ByteBuffer.allocate(sampleSize);
            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();
            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            muxer.start();
            while (!sawEOS) {
                videoBufferInfo.offset = offset;
                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset);
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    sawEOS = true;
                    videoBufferInfo.size = 0;
                } else {
                    videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                    //noinspection WrongConstant
                    videoBufferInfo.flags = videoExtractor.getSampleFlags();
                    muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo);
                    videoExtractor.advance();
                    frameCount++;
                }
            }

            boolean sawEOS2 = false;
            int frameCount2 = 0;
            while (!sawEOS2) {
                frameCount2++;
                audioBufferInfo.offset = offset;
                audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset);
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    sawEOS2 = true;
                    audioBufferInfo.size = 0;
                } else {
                    audioBufferInfo.presentationTimeUs = audioExtractor.getSampleTime();

                    audioBufferInfo.flags = audioExtractor.getSampleFlags();
                    muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo);
                    audioExtractor.advance();
                }
            }
            muxer.stop();
            muxer.release();
            audioExtractor.release();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

