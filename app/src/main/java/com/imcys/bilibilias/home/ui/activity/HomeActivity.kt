package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.common.di.AsCookiesStorage
import com.imcys.bilibilias.databinding.ActivityHomeBinding
import com.imcys.bilibilias.home.ui.adapter.MyFragmentPageAdapter
import com.imcys.bilibilias.home.ui.fragment.DownloadFragment
import com.imcys.bilibilias.home.ui.fragment.HomeFragment
import com.imcys.bilibilias.home.ui.fragment.ToolFragment
import com.imcys.bilibilias.home.ui.fragment.UserFragment

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private var exitTime: Long = 0
    lateinit var activityHomeBinding: ActivityHomeBinding

    lateinit var toolFragment: ToolFragment
    lateinit var homeFragment: HomeFragment
    lateinit var downloadFragment: DownloadFragment
    lateinit var userFragment: UserFragment

    @Inject
    lateinit var asCookiesStorage: AsCookiesStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        备选方案
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        mPaint.setColorFilter(ColorMatrixColorFilter(cm))
        window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
         */

        // 补全必须要的内容
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        initFragment()
        loadFragment()

        parseShare()

        // 启动百度统计
        startBaiDuService()
    }

    /**
     * 初始化fragment
     */
    private fun initFragment() {
        homeFragment = HomeFragment.newInstance()
        toolFragment = ToolFragment.newInstance()
        userFragment = UserFragment.newInstance()
        downloadFragment = DownloadFragment.newInstance()
    }

    // 启动时解析视频数据
    @SuppressLint("ResourceType")
    private fun parseShare() {
        val intent = intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                activityHomeBinding.apply {
                    homeViewPage.currentItem = 1
                    homeBottomNavigationView.menu.getItem(1).isChecked = true
                    toolFragment.parseShare(intent)
                }
            }
        }
        if (Intent.ACTION_CREATE_SHORTCUT == intent?.action) {
            activityHomeBinding.apply {
                homeViewPage.currentItem = 1
                homeBottomNavigationView.menu.getItem(1).isChecked = true
                toolFragment.parseShare(intent)
            }
        }
    }

    // 复用/创建时检测
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                activityHomeBinding.apply {
                    homeViewPage.currentItem = 1
                    homeBottomNavigationView.menu.getItem(1).isChecked = true
                    toolFragment.parseShare(intent)
                }
            }
        }
        val asUrl = intent.extras?.getString("asUrl")
        if (asUrl != null) {
            activityHomeBinding.apply {
                homeViewPage.currentItem = 1
                homeBottomNavigationView.menu.getItem(1).isChecked = true
                toolFragment.parseShare(intent)
            }
        }
    }

    // 加载fragment
    private fun loadFragment() {
        val fragmentArrayList = ArrayList<Fragment>()
        // 添加fragment
        fragmentArrayList.add(homeFragment)
        fragmentArrayList.add(toolFragment)
        fragmentArrayList.add(downloadFragment)
        fragmentArrayList.add(userFragment)

        val myFragmentPageAdapter =
            MyFragmentPageAdapter(supportFragmentManager, lifecycle, fragmentArrayList)
        activityHomeBinding.let {
            it.homeViewPage.adapter = myFragmentPageAdapter
            it.homeViewPage.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                // 滚动监听选择
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    it.homeBottomNavigationView.menu.getItem(position).isChecked = true
                }
            })

            it.homeViewPage.isUserInputEnabled = false

            // 点击监听
            it.homeBottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home_bottom_menu_black_room -> {
                        it.homeViewPage.currentItem = 0
                        it.homeBottomNavigationView.menu.getItem(0).isChecked = true
                    }

                    R.id.home_bottom_menu_discipline_admin -> {
                        it.homeViewPage.currentItem = 1
                        it.homeBottomNavigationView.menu.getItem(1).isChecked = true
                    }

                    R.id.home_bottom_menu_operation_log -> {
                        it.homeViewPage.currentItem = 2
                        it.homeBottomNavigationView.menu.getItem(2).isChecked = true
                    }

                    R.id.home_bottom_menu_statistics -> {
                        it.homeViewPage.currentItem = 3
                        it.homeBottomNavigationView.menu.getItem(3).isChecked = true
                    }
                }
                false
            }
        }
    }

    /**
     * 百度统计
     */
    fun startBaiDuService() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val authorizedState = sharedPreferences.getBoolean("baidu_statistics_type", false)
        StatService.setAuthorizedState(this, authorizedState)
        StatService.start(this)
    }

    companion object {

        fun actionStart(context: Context, asUrl: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("asUrl", asUrl)
            context.startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        asCookiesStorage.close()
    }

    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(
                applicationContext,
                getString(R.string.app_HomeActivity_exit),
                Toast.LENGTH_SHORT
            ).show()
            exitTime = System.currentTimeMillis()
        } else {
            finishAll()
        }
    }
}
