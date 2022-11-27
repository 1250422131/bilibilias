package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asLogE
import com.imcys.bilibilias.base.view.AsJzvdStd
import com.imcys.bilibilias.base.view.JzbdStdInfo
import com.imcys.bilibilias.danmaku.BiliDanmukuParser
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.adapter.SubsectionAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.utils.HttpUtils
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.loader.IllegalDataException
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.*
import java.util.zip.Inflater


class AsVideoActivity : BaseActivity() {


    lateinit var binding: ActivityAsVideoBinding

    lateinit var asJzvdStd: AsJzvdStd
    lateinit var asDanmaku: IDanmakuView
    lateinit var danmakuParser: BaseDanmakuParser
    val danmakuContext = DanmakuContext.create()

    //视频临时数据
    var bvid: String = ""
    var avid: Int = 0
    var cid: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_as_video)
        statusBarOnly(this)

        //加载视频首要信息
        initVideoData()

        initView()


    }

    private fun initView() {


        binding.apply {

            asJzvdStd = asVideoAsJzvdStd
            asDanmaku = asVideoAsJzvdStd.asDanmaku

            asVideoShareLy.setOnClickListener {
                loadShareView()
            }

            asVideoFaButton.setOnClickListener {
                when (asJzvdStd.state) {
                    Jzvd.STATE_NORMAL, Jzvd.STATE_AUTO_COMPLETE -> {
                        asJzvdStd.startVideo()
                    }
                    Jzvd.STATE_PAUSE, Jzvd.STATE_PLAYING -> {
                        asJzvdStd.startButton.performClick()
                    }
                }
            }


        }
    }

    /**
     * 加载视频播放信息
     */
    private fun loadVideoPlay() {

        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=0&fourk=1",
                VideoPlayBean::class.java) {
                setAsJzvdConfig(it.data.durl[0].url, "")

            }


    }


    /**
     * 加载用户收藏夹
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun loadShareView() {
        binding.apply {
            HttpUtils.addHeader("cookie", App.cookies)
                .get(BilibiliApi.userCreatedScFolderPath + "?up_mid=" + App.mid,
                    UserCreateCollectionBean::class.java) {
                    if (it.code == 0) {
                        DialogUtils.loadUserCreateCollectionDialog(this@AsVideoActivity,
                            it) { selectedItem, selects ->
                        }.show()
                    }
                }
        }

    }

    /**
     * 加载视频数据
     */
    private fun initVideoData() {


        val intent = intent
        var bvId = intent.getStringExtra("bvId")
        //bvId = "BV1ss411m7t9"

        HttpUtils.addHeader("cookie", App.cookies)
            .get(BilibiliApi.getVideoDataPath + "?bvid=$bvId", VideoBaseBean::class.java) {

                showVideoData()
                //TODO 设置基本数据，注意这里必须优先，因为我们在后面会复用这些数据
                setBaseData(it)
                //加载用户卡片
                loadUserCardData(it.data.owner.mid)
                //加载弹幕信息
                loadDanmakuFlameMaster()
                //加载视频播放信息
                loadVideoPlay()
                //加载视频列表信息
                loadVideoList()
            }
    }

    /**
     * 加载视频列表信息
     *
     */
    private fun loadVideoList() {
        HttpUtils.get(BilibiliApi.videoPageListPath + "?bvid=" + bvid,
            VideoPageListData::class.java) {
            binding.apply {

                if (it.data.size == 0) asVideoSubsectionRv.visibility = View.GONE

                asVideoSubsectionRv.adapter =
                    SubsectionAdapter(it.data.toMutableList()) { data, position ->

                        //更新CID刷新播放页面
                        cid = data.cid
                        //暂停播放
                        changeFaButtonToPlay()
                        //刷新播放器
                        loadVideoPlay()
                        //清空弹幕
                        asDanmaku.release()
                        //更新弹幕
                        loadDanmakuFlameMaster()
                        asVideoSubsectionRv.adapter?.notifyItemChanged(position)


                    }

                asVideoSubsectionRv.layoutManager =
                    LinearLayoutManager(this@AsVideoActivity, LinearLayoutManager.HORIZONTAL, false)


            }
        }

    }

    /**
     * 写入基本变量数据
     * @param videoBaseBean VideoBaseBean
     */
    private fun setBaseData(videoBaseBean: VideoBaseBean) {
        bvid = videoBaseBean.data.bvid
        avid = videoBaseBean.data.aid
        cid = videoBaseBean.data.cid
        binding.videoBaseBean = videoBaseBean
    }


    /**
     * 加载弹幕信息
     */
    private fun loadDanmakuFlameMaster() {

        HttpUtils.addHeader("cookie", App.cookies).get("${BilibiliApi.videoDanMuPath}?oid=$cid",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    asLogE(this@AsVideoActivity, "请求错误")
                }

                override fun onResponse(call: Call, response: Response) {

                    App.handler.post {
                        //储存弹幕
                        saveDanmaku(response.body!!.bytes())
                        //初始化弹幕配置
                        initDanmaku()
                    }
                }

            })
    }


    private fun initDanmaku() {
        val input: InputStream =
            FileInputStream(getExternalFilesDir("temp").toString() + "/tempDm.xml")

        //解析弹幕
        danmakuParser = createParser(input)!!


        //设置弹幕配置
        setDanmakuContextCongif()

        //设置弹幕监听器
        setAsDanmakuCallback()

    }


    private fun loadUserCardData(mid: Long) {
        HttpUtils.addHeader("cookie", App.cookies)
            .get(BilibiliApi.getUserCardPath + "?mid=$mid", UserCardBean::class.java) {
                showUserCard()
                binding.userCardBean = it
            }

    }

    /**
     * 显示用户卡片
     */
    private fun showUserCard() {
        binding.apply {
            asVideoUserCardLy.visibility = View.VISIBLE
        }
    }

    /**
     * 显示视频数据页面
     */
    private fun showVideoData() {
        binding.apply {
            asVideoDataLy.visibility = View.VISIBLE
        }
    }


    override fun onBackPressed() {
        if (JzvdStd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        asDanmaku.apply {
            pause()
            // hide()
        }
        JzvdStd.releaseAllVideos()
        changeFaButtonToPlay()
        super.onPause()
    }

    companion object {

        fun actionStart(context: Context, bvId: String) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", bvId)
            context.startActivity(intent)
        }

        @Deprecated("B站已经在弱化aid的使用，我们不确定这是否会被弃用，因此这个方法将无法确定时效性")
        fun actionStart(context: Context, aid: Int) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("aid", aid)
            context.startActivity(intent)
        }
    }


    /**
     * 设置饺子播放器参数配置
     * @param url String
     * @param title String
     */
    private fun setAsJzvdConfig(url: String, title: String) {
        val map = LinkedHashMap<Any, Any>()

        val jzDataSource = JZDataSource(url, title)

        jzDataSource.headerMap["Cookie"] = App.cookies;
        jzDataSource.headerMap["Referer"] = "https://www.bilibili.com/video/$bvid";
        jzDataSource.headerMap["User-Agent"] =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0";

        asJzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL)

        asJzvdStd.setPlayStateListener(object : JzbdStdInfo {

            override fun statePlaying(state: Int) {

                changeFaButtonToPause()

                asDanmaku.apply {

                    if (state == Jzvd.STATE_PAUSE) {
                        if (asJzvdStd.stopTime != asJzvdStd.currentPositionWhenPlaying) {
                            start(asJzvdStd.currentPositionWhenPlaying)
                        } else {
                            resume()
                        }

                    } else {
                        prepare(danmakuParser, danmakuContext)
                        enableDanmakuDrawingCache(true)
                    }
                }

            }

            override fun stopPlay(state: Int) {
                changeFaButtonToPlay()
                //暂停弹幕
                asDanmaku.pause()


            }

            override fun endPlay(state: Int) {
                changeFaButtonToRedo()
            }

            override fun seekBarStopTracking(state: Int) {
                if (state == Jzvd.STATE_PLAYING) {
                    asDanmaku.start(asJzvdStd.currentPositionWhenPlaying)
                }
            }


        })

    }


    //——————————————————————————————————————————————————————————————————————————
    //悬浮按钮状态更新
    private fun changeFaButtonToPlay() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_play)
    }

    private fun changeFaButtonToPause() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_pause)
    }

    private fun changeFaButtonToRedo() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_redo)
    }
    //——————————————————————————————————————————————————————————————————————————


    //——————————————————————————————————————————————————————————————————————————
    //弹幕抽离
    /**
     * 储存弹幕内容
     */
    private fun saveDanmaku(bytes: ByteArray) {

        val bufferedSink: BufferedSink?
        val dest = File(getExternalFilesDir("temp").toString(), "tempDm.xml")
        if (!dest.exists()) dest.createNewFile()
        val sink = dest.sink() //打开目标文件路径的sink
        val decompressBytes =
            decompress(bytes) //调用解压函数进行解压，返回包含解压后数据的byte数组
        bufferedSink = sink.buffer()
        decompressBytes?.let { bufferedSink.write(it) } //将解压后数据写入文件（sink）中
        bufferedSink.close()

    }

    /**
     * 配置弹幕信息
     */

    private fun setDanmakuContextCongif() {

        // 设置弹幕的最大显示行数
        val maxLinesPair = HashMap<Int, Int>()
        //maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        val overlappingEnablePair = HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_LR] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_BOTTOM] = true

        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3F) //设置描边样式
            .setDuplicateMergingEnabled(false)
            .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
            .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
            .setMaximumLines(maxLinesPair) //设置最大显示行数
            .preventOverlapping(overlappingEnablePair) //设置防弹幕重叠，null为允许重叠

    }

    /**
     * 配置弹幕监听器
     */
    private fun setAsDanmakuCallback() {

        asDanmaku.setCallback(object : master.flame.danmaku.controller.DrawHandler.Callback {
            override fun prepared() {
                //弹幕准备好的时候回调，这里启动弹幕
                asDanmaku.start()
            }

            override fun updateTimer(timer: DanmakuTimer?) {
                //定时器更新的时候回调
            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {
                //弹幕展示的时候回调
            }

            override fun drawingFinished() {
                //弹幕绘制完成时回调
            }

        })

    }


    /**
     * 创建解析器对象，解析输入流
     * @param stream
     * @return
     */
    private fun createParser(stream: InputStream?): BaseDanmakuParser? {
        if (stream == null) {
            return object : BaseDanmakuParser() {
                override fun parse(): Danmakus {
                    return Danmakus()
                }
            }
        }
        val loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI)
        try {
            loader.load(stream)
        } catch (e: IllegalDataException) {
            e.printStackTrace()
        }
        val parser: BaseDanmakuParser = BiliDanmukuParser()
        val dataSource = loader.dataSource
        parser.load(dataSource)
        return parser
    }


    //——————————————————————————————————————————————————————————————————————————

    override fun onResume() {
        super.onResume()
        if (asDanmaku.isPrepared && asDanmaku.isPaused) {
            //asDanmaku.show()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        asDanmaku.release()
    }


    //解压deflate数据的函数
    fun decompress(data: ByteArray): ByteArray? {
        var output: ByteArray
        val decompresser = Inflater(true) //这个true是关键
        decompresser.reset()
        decompresser.setInput(data)
        val o = ByteArrayOutputStream(data.size)
        try {
            val buf = ByteArray(1024)
            while (!decompresser.finished()) {
                val i: Int = decompresser.inflate(buf)
                o.write(buf, 0, i)
            }
            output = o.toByteArray()
        } catch (e: Exception) {
            output = data
            e.printStackTrace()
        } finally {
            try {
                o.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        decompresser.end()
        return output
    }


}