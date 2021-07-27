package com.imcys.bilibilias.as;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mobstat.StatService;
import com.imcys.bilibilias.BilibiliPost;
import com.imcys.bilibilias.R;
import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;

import java.io.IOException;

public class MergeVideoActivity extends AppCompatActivity{
    private String videoPath;
    private String audioPath;
    private String DLPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mergevideo);
        try {
            //获取下载路径
            DLPath = BilibiliPost.fileRead(getExternalFilesDir("下载设置").toString()+"/Path.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void goMergeVideo(View view) {
        if ( videoPath != null || audioPath != null) {
            ProgressDialog hbDialog = ProgressDialog.show(MergeVideoActivity.this, "提示", "正在合并文件\n并不能保证每次都成功，但根据经验，小点的视频容易成功。");
            new Thread(() -> {
                MediaExtractorUtils.startComposeTrack(videoPath, audioPath,  videoPath + "_合并" + ".mp4");
                runOnUiThread(() -> {
                    hbDialog.cancel();
                    Toast.makeText(MergeVideoActivity.this,"合并完成",Toast.LENGTH_SHORT).show();
                });
            }).start();
        }else {
            Toast.makeText(getApplicationContext(), "请选择完整路径", Toast.LENGTH_SHORT).show();
        }
    }

    public void setVideoPath(View view){
        FileChooser fileChooser = new FileChooser(MergeVideoActivity.this, new FileChooser.FileChoosenListener() {
            @Override
            public void onFileChoosen(String filePath) {
                ((TextView) findViewById(R.id.Play_VideoPath_TextView)).setText(filePath);
                videoPath = filePath;
            }
        });

        fileChooser.setTitle("选择视频文件路径");
        fileChooser.setDoneText("确定");
        fileChooser.setThemeColor(R.color.colorAccent);
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
        FileChooser fileChooser = new FileChooser(MergeVideoActivity.this, new FileChooser.FileChoosenListener() {
            @Override
            public void onFileChoosen(String filePath) {
                ((TextView) findViewById(R.id.Play_DmPath_TextView)).setText(filePath);
                audioPath = filePath;
            }
        });
        fileChooser.setTitle("选择音频文件路径");
        fileChooser.setDoneText("确定");
        fileChooser.setThemeColor(R.color.colorAccent);
        fileChooser.setChooseType(FileInfo.FILE_TYPE_FILE);
        fileChooser.showFile(true);  //是否显示文件
        fileChooser.open();
    }
}
