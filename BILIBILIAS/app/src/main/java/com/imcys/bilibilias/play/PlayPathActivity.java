package com.imcys.bilibilias.play;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.R;
import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;

import java.io.IOException;

public class PlayPathActivity extends AppCompatActivity {

    private String videoPath;
    private String dmPath;
    private String DLPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_path);
        try {
            //获取下载路径
            DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/Path.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goPlayVideo(View view) {
        if ( videoPath != null || dmPath != null) {
            Intent intent = new Intent();
            intent.setClass(PlayPathActivity.this, PlayVideoActivity.class);
            intent.putExtra("videoPath", videoPath);
            intent.putExtra("dmPath", dmPath);
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "请选择完整路径", Toast.LENGTH_SHORT).show();
        }
    }

    public void setVideoPath(View view){
        FileChooser fileChooser = new FileChooser(PlayPathActivity.this, new FileChooser.FileChoosenListener() {
            @Override
            public void onFileChoosen(String filePath) {
                ((TextView) findViewById(R.id.Play_VideoPath_TextView)).setText(filePath);
                videoPath = filePath;
            }
        });

        fileChooser.setTitle("选择视频文件路径");
        fileChooser.setDoneText("确定");
        fileChooser.setThemeColor(R.color.colorAccent);
        fileChooser.setCurrentPath(DLPath);
        //                          FILE_TYPE_FOLDER="type_folder";  //文件夹
        //                          FILE_TYPE_VIDEO="type_video";    //视频
        //                          FILE_TYPE_AUDIO="type_audio";    //音频
        //                          FILE_TYPE_FILE="type_file";      //全部文件
        //                          FILE_TYPE_APK="type_apk";        //apk
        //                          FILE_TYPE_ZIP="type_zip";        //zip
        //                          FILE_TYPE_RAR="type_rar";        //rar
        //                          FILE_TYPE_JPEG="type_jpeg";      //jpeg
        //                          FILE_TYPE_JPG="type_jpg";         //jpg
        //                          FILE_TYPE_PNG="type_png";         //png
        //
        //                          FILE_TYPE_ALL="type_all";         //所有文件
        //                           FILE_TYPE_IMAGE="type_image";    //所有图片
        //                           FILE_TYPE_PACKAGE="type_package";  //压缩包

        fileChooser.setChooseType(FileInfo.FILE_TYPE_FILE);
        fileChooser.showFile(true);  //是否显示文件
        fileChooser.open();

    }

    public void setDmPath(View view){
        FileChooser fileChooser = new FileChooser(PlayPathActivity.this, new FileChooser.FileChoosenListener() {
            @Override
            public void onFileChoosen(String filePath) {
                ((TextView) findViewById(R.id.Play_DmPath_TextView)).setText(filePath);
                dmPath = filePath;
            }
        });
        fileChooser.setTitle("选择弹幕文件路径");
        fileChooser.setDoneText("确定");
        fileChooser.setCurrentPath(DLPath);
        fileChooser.setThemeColor(R.color.colorAccent);
        fileChooser.setChooseType(FileInfo.FILE_TYPE_FILE);
        fileChooser.showFile(true);  //是否显示文件
        fileChooser.open();
    }

}
