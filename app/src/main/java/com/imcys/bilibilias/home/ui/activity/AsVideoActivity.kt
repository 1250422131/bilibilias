package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.adapter.CreateCollectionAdapter
import com.imcys.bilibilias.home.ui.model.UserCardBean
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.imcys.bilibilias.utils.HttpUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

class AsVideoActivity : BaseActivity() {


    lateinit var binding: ActivityAsVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_as_video)
        statusBarOnly(this)



        initView()

        initVideoData()

    }

    private fun initView() {

        binding.apply {
            asVideoShareLy.setOnClickListener {
                loadShareView()
            }
        }
    }

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
                binding.videoBaseBean = it
                loadUserCardData(it.data.owner.mid)
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
}