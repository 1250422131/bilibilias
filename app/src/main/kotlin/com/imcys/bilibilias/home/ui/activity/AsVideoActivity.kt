package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.view.AppAsJzvdStd
import com.imcys.bilibilias.common.base.utils.ConversionUtil
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.home.ui.viewmodel.AsVideoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AsVideoActivity : BaseActivity<ActivityAsVideoBinding>() {
    override val layoutId = R.layout.activity_as_video
    private val TAG = this.javaClass.name

    // 视频基本数据类，方便全局调用
    private lateinit var videoDataBean: VideoBaseBean

    // 饺子播放器，方便全局调用
    private lateinit var asJzvdStd: AppAsJzvdStd

    lateinit var userBaseBean: UserBaseBean

    private val asVideoViewModel    : AsVideoViewModel by viewModels()


    // 视频临时数据，方便及时调用，此方案考虑废弃
    var bvid: String = ""
    var avid: Long = 0L
    var cid: Long = 0L
    var epid: Long = 0L

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 加载用户信息&视频信息
        loadUserData()
        // 加载控件
        initView()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(newBase)
//
//    }
    /**
     * 加载用户信息，为了确保会员视频及时通知用户
     */
    private fun loadUserData() {
    }

     override fun initView() {
        binding.apply {
            // 绑定播放器，弹幕控制器
            asJzvdStd = asVideoAsJzvdStd

            // 设置播放按钮功能
            asVideoFaButton.setOnClickListener {
                when (asJzvdStd.state) {
                    Jzvd.STATE_NORMAL, Jzvd.STATE_AUTO_COMPLETE -> {
                        // 播放视频
                        asVideoFaButton.visibility = View.GONE
                        asJzvdStd.startVideo()
                    }

                    Jzvd.STATE_PAUSE, Jzvd.STATE_PLAYING -> {
                        // 恢复播放/暂停播放
                        asJzvdStd.startButton.performClick()
                    }
                }
            }
        }
    }

    /**
     * 加载视频播放信息
     */
    private fun loadVideoPlay(type: String) {

    }

    /**
     * 加载视频数据
     */
    private fun initVideoData() {

    }

    /**
     * 检查三连情况
     */
    private fun archiveHasLikeTriple() {

    }

    /**
     * 收藏检验
     */
    private suspend fun archiveFavoured() {
    }

    /**
     * 检验投币情况
     */
    private suspend fun archiveCoins() {
    }

    /**
     * 检验是否点赞
     */
    private suspend fun archiveHasLike() {
    }

    /**
     * 更新番剧信息
     * @param data EpisodesBean
     * @param position Int
     */
    private fun updateBangumiInformation(
        data: BangumiSeasonBean.ResultBean.EpisodesBean,
    ) {
        val userVipState = userBaseBean.data.vip.status
        if (data.badge == getString(R.string.app_asvideoactivity_updatebangumiinformation_badge) && userVipState != 1) {

        } else {
            // 更新CID刷新播放页面
            cid = data.cid
            bvid = data.bvid
            epid = data.id

            // 更新海报->确保可以下载每一个子集的海报
            asJzvdStd.updatePoster(data.cover)
            // 暂停播放
            changeFaButtonToPlay()
            // 刷新播放器
            loadVideoPlay("bangumi")
        }
    }

    /**
     * 会员检测，确保用户有会员，或者无会员的提示
     * @param bangumiSeasonBean BangumiSeasonBean
     */
    private fun isMember(bangumiSeasonBean: BangumiSeasonBean) {
        var memberType = false

        val userVipState = userBaseBean.data.vip.status
        bangumiSeasonBean.result.episodes.forEach {
            if (it.cid == cid && it.badge == getString(R.string.app_asvideoactivity_ismember_badge) && userVipState != 1) memberType = true
        }
        if (memberType) {

        } else {
            loadVideoPlay("bangumi")
        }
    }

    /**
     * 显示用户卡片
     */
    private fun showUserCard() {
        binding.apply {
            asVideoUserCardLy.visibility = View.VISIBLE

            // 判断是否会员，会员情况下展示会员主题色，反之黑色
            val nameColor = if (userBaseBean.data.vip.nickname_color != "") {
                Color.parseColor(userBaseBean.data.vip.nickname_color)
            } else {
                // 低版本兼容
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getColor(R.color.black)
                } else {
                    Color.BLACK
                }
            }

            // 设置最终获取颜色
            asVideoUserName.setTextColor(nameColor)
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // 释放播放器
        if (JzvdStd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        if (asJzvdStd.state == Jzvd.STATE_PLAYING) { // 暂停视频
            asJzvdStd.startButton.performClick()
            changeFaButtonToPlay()
        }
    }

    companion object {

        fun actionStart(context: Context, bvId: String) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", bvId)
            context.startActivity(intent)
        }

        @Deprecated("B站已经在弱化aid的使用，我们不确定这是否会被弃用，因此这个方法将无法确定时效性")
        fun actionStart(context: Context, aid: Long) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", ConversionUtil.av2bv(aid))
            context.startActivity(intent)
        }
    }

    /**
     * 设置饺子播放器参数配置
     * @param url String
     * @param title String
     */
    private fun setAsJzvdConfig(url: String, title: String) {
    }

    // ——————————————————————————————————————————————————————————————————————————
    // 悬浮按钮状态更新
    private fun changeFaButtonToPlay() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_play)
    }

    private fun changeFaButtonToPause() {
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_pause)
    }

    private fun changeFaButtonToRedo() {
        binding.asVideoFaButton.isVisible = true
        binding.asVideoFaButton.setImageResource(R.drawable.ic_as_video_redo)
    }
}
