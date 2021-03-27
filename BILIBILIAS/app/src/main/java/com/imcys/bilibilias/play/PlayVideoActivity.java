package com.imcys.bilibilias.play;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.imcys.bilibilias.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

public class PlayVideoActivity extends AppCompatActivity {
    private String videoPath;
    private String dmPath;
    private BaseDanmakuParser mParser;//解析器对象
    private IDanmakuView mDanmakuView;
    private DanmakuContext mContext;
    private PlayerView playerView;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        //隐藏自带标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.hide();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

        Intent intent = getIntent();
        videoPath = intent.getStringExtra("videoPath");
        dmPath = intent.getStringExtra("dmPath");
        playVideo();
        try {
            dmNew();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void playVideo(){
        playerView = (PlayerView)findViewById(R.id.Play_Video_PlayerView);
        // 创建带宽
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // 创建轨道选择工厂
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        // 创建轨道选择实例
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        // 创建播放器实例
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        playerView.setPlayer(player);
        // 创建加载数据的工厂
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this,"MyApplication"),null);
        Uri uri = Uri.parse("file://"+ videoPath);
        // 创建资源
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        // 准备播放
        player.prepare(mediaSource);
        // 开始播放
        player.setPlayWhenReady(true);
    }


    private void dmNew() throws FileNotFoundException {
        mDanmakuView = (IDanmakuView) findViewById(R.id.Play_Video_DanmakuView);

        mContext = DanmakuContext.create();

        // 设置弹幕的最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        //maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM, true);

        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3) //设置描边样式
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
                .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair); //设置防弹幕重叠，null为允许重叠


        if (mDanmakuView != null) {
            InputStream input =  new FileInputStream(dmPath);
            mParser = createParser(input); //创建解析器对象，从raw资源目录下解析comments.xml文本
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });

            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(false); //是否显示FPS
            mDanmakuView.enableDanmakuDrawingCache(true);
        }

    }

    /**
     * 创建解析器对象，解析输入流
     * @param stream
     * @return
     */
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }


    @Override
    protected void onResume () {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() &&
                mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
        //弹幕内存释放
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }
}
