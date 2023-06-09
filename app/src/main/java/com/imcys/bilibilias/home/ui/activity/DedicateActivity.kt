package com.imcys.bilibilias.home.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityDedicateBinding
import com.imcys.bilibilias.home.ui.adapter.DedicateAdapter
import com.imcys.bilibilias.home.ui.model.DedicateBean
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import javax.inject.Inject

class DedicateActivity : BaseActivity() {
    lateinit var binding: ActivityDedicateBinding

    @Inject
    lateinit var adapter: DedicateAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityDedicateBinding?>(
            this,
            R.layout.activity_dedicate
        ).apply {

            dedicateTopLy.addStatusBarTopPadding()
        }

        initView()
    }


    private fun initView() {

        loadDedicate()

    }

    private fun loadDedicate() {
        val dedicateMutableList = mutableListOf(
            DedicateBean(
                "BILIBILIAS捐款名单",
                "粉丝为BILIBILIAS提供的捐款支持",
                "长期以来，BILIBILIAS依靠着粉丝的捐款维持着服务器的运作，再次感谢所有捐款人员。\n也许现在页面简陋，但是我们在着手改造。",
                "https://view.misakamoe.com/v1/app_ico.png",
                "https://api.misakamoe.com/as-donate.html"
            ),
            DedicateBean(
                "GitHub",
                "欢迎点击 star",
                "BILIBILIAS开源开发，可以前去监督开发和提交需求，你还可以通过分叉来参与开发。",
                "http://message.biliimg.com/bfs/im/1903c78f14847ad1d17a2f828d336aa80977d3c0.png",
                "https://github.com/1250422131/bilibilias"
            ),
            DedicateBean(
                "萌新交流社",
                "BILIBILIAS反馈群",
                "感谢萌新交流社的风纪委员会全体成员，几年来不离不弃，尽职尽责的维护着群内治安。",
                "http://p.qlogo.cn/gh/812128563/812128563/640/",
                "https://qm.qq.com/cgi-bin/qm/qr?k=NYCx2UMEC0yBvi2YYsPHSGge3b5Lolv4&jump_from=webapi&authKey=/Ag6VpX3P8PczmAD80GE2nuMUNkvY+ra6N8s15IzgxdPts4b4gcUg60w5EAuzSHa"
            ),
            DedicateBean(
                "萌新杰少 | IMCYS",
                "BILIBILIAS开发",
                "点击去我的博客看看？",
                "https://q1.qlogo.cn/g?b=qq&nk=1250422131&s=640",
                "https://imcys.com/"
            ),
            DedicateBean(
                "戴兜丫 | im.daidr.m",
                "UI指导 & APP图标设计",
                "戴兜在BILIBILIAS新版本设计时提供了许多的UI指导，并且还为BILIBILIAS设计了图标，非常感谢戴兜对本APP设计支持，点击可以去博客援助他。",
                "https://q1.qlogo.cn/g?b=qq&nk=2770582393&s=640",
                "https://im.daidr.me/"
            ),
            DedicateBean(
                "Misaka | 御坂呱太",
                "提供后端维护&支持",
                "呱太自BILIBILIAS后端运维之初，就提供着后端的服务器支持，BILIBILIAS前期的统计离不开呱太的支持。",
                "https://q1.qlogo.cn/g?b=qq&nk=208075801&s=640",
                "https://misakamoe.com/"
            ),
            DedicateBean(
                "xiwangly | 希望",
                "BILIBILIAS多语言翻译",
                "希望为as的国际化做出了巨大贡献，提供了英语，繁体等语言翻译。另外在在各个版本都提出了不少的意见，部分意见已经实现。",
                "https://q1.qlogo.cn/g?b=qq&nk=1334850101&s=640",
                "https://www.xiwangly.top/"
            ),
            DedicateBean(
                "橘凤蝶",
                "BILIBILIAS论坛长期优质反馈人员",
                "橘凤蝶BILIBILIAS的论坛发布了许多有价值的意见，在新版本已经得到了体现.",
                "https://q1.qlogo.cn/g?b=qq&nk=294774190&s=640",
                ""
            ),
            DedicateBean(
                "紫叶",
                "部分APP建议",
                "紫叶为BILIBILIAS的开发提供的一些重要的意见，部分已经实现。",
                "https://q1.qlogo.cn/g?b=qq&nk=3287319782&s=640",
            ),
            DedicateBean(
                "空dio徐伦",
                "帮助BILIBILIAS在其他平台展示",
                "空dio徐伦，长期在App分享软件里提交BILIBILIAS的更新，只是很遗憾我们没办法联系到他，这里头像就先使用app的吧。",
                "https://view.misakamoe.com/v1/app_ico.png",
                ""
            ),
        )
        binding.apply {
            dedicateRv.adapter = adapter
            dedicateRv.layoutManager =
                LinearLayoutManager(this@DedicateActivity, LinearLayoutManager.VERTICAL, false)
            adapter.submitList(dedicateMutableList)
        }
    }


    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }
}