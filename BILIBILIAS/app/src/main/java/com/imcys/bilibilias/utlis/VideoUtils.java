package com.imcys.bilibilias.utlis;

import android.media.MediaPlayer;

import java.io.IOException;

public class VideoUtils {

    public static int getLocalVideoDuration(String videoPath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepare();
            long time = mediaPlayer.getDuration();//获得了视频的时长（以毫秒为单位）
            return (int) time;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
