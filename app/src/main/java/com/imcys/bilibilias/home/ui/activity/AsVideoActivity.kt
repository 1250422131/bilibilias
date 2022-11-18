package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import cn.jzvd.JZDataSource
import cn.jzvd.JzvdStd
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.asLogD
import com.imcys.bilibilias.base.view.AsJzvdStd
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.model.UserCardBean
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.imcys.bilibilias.home.ui.model.VideoPlayBean
import com.imcys.bilibilias.utils.HttpUtils
import java.util.regex.Matcher
import java.util.regex.Pattern


class AsVideoActivity : BaseActivity() {


    lateinit var binding: ActivityAsVideoBinding
    lateinit var videoDataBean: VideoBaseBean.DataBean
    lateinit var videoPlayBean: VideoPlayBean

    lateinit var asJzvdStd: AsJzvdStd

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

            asVideoShareLy.setOnClickListener {
                loadShareView()
            }


        }
    }

    private fun loadVideoPlay() {

        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.videoPlayPath}?bvid=$bvid&cid=$cid&qn=64&fnval=0",
                VideoPlayBean::class.java) {


                val jzDataSource = JZDataSource(it.data.durl[0].url, "测速")

                jzDataSource.headerMap["Cookie"] = App.cookies;
                jzDataSource.headerMap["Referer"] = "https://www.bilibili.com/video/$bvid";
                jzDataSource.headerMap["User-Agent"] =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0";

                asJzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL)


            }


    }


    //加载用户收藏夹
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

    private fun initVideoData() {


        val intent = intent
        val bvId = intent.getStringExtra("bvId")

        HttpUtils.addHeader("cookie", App.cookies)
            .get(BilibiliApi.getVideoDataPath + "?bvid=$bvId", VideoBaseBean::class.java) {
                showVideoData()
                //数据集合
                bvid = it.data.bvid
                avid = it.data.aid
                cid = it.data.cid
                binding.videoBaseBean = it

                //加载用户卡片
                loadUserCardData(it.data.owner.mid)
                //加载视频播放信息
                loadVideoPlay()
            }
    }

    private fun loadUserCardData(mid: Long) {
        HttpUtils.addHeader("cookie", App.cookies)
            .get(BilibiliApi.getUserCardPath + "?mid=$mid", UserCardBean::class.java) {
                showUserCard()
                binding.userCardBean = it
            }

    }


    //获取用户放入待解析内容的解析结果
    private fun getAsBvid(asString: String): String {

        //BVID数据抽离
        val patternTj = "(bV|BV|Bv|bv)[a-zA-Z0-9]{10}"
        val pattern: Pattern = Pattern.compile(patternTj)
        val matcher: Matcher = pattern.matcher(asString)
        return matcher.group()

    }


    private fun showUserCard() {
        binding.apply {
            asVideoUserCardLy.visibility = View.VISIBLE
        }
    }

    //显示视频数据页面
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
        JzvdStd.releaseAllVideos()
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

}