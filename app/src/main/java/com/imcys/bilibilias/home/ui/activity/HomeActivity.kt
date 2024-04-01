package com.imcys.bilibilias.home.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.viewpager2.widget.ViewPager2
import com.hjq.toast.Toaster
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.databinding.ActivityHomeBinding
import com.imcys.bilibilias.home.ui.adapter.MyFragmentPageAdapter
import com.imcys.bilibilias.home.ui.fragment.DownloadFragment
import com.imcys.bilibilias.home.ui.fragment.HomeFragment
import com.imcys.bilibilias.home.ui.fragment.ToolFragment
import com.imcys.bilibilias.home.ui.fragment.UserFragment
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.anno.RouterAnno
import dagger.hilt.android.AndroidEntryPoint

@RouterAnno(hostAndPath = ARouterAddress.AppHomeActivity,)
@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override val layoutId: Int = R.layout.activity_home
    private var exitTime: Long = 0

    private lateinit var toolFragment: ToolFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var downloadFragment: DownloadFragment
    private lateinit var userFragment: UserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Component.inject(target = this)
        initFragment()
        loadFragment()
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEND == intent.action && "text/plain" == intent.type) {
            binding.homeViewPage.currentItem = 1
            binding.homeBottomNavigationView.menu.getItem(1).isChecked = true
            toolFragment.parseShare(intent)
        }
    }

    // 加载fragment
    private fun loadFragment() {
        val fragmentArrayList = listOf(homeFragment, toolFragment, downloadFragment, userFragment)
        val myFragmentPageAdapter =
            MyFragmentPageAdapter(supportFragmentManager, lifecycle, fragmentArrayList)
        binding.let {
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - exitTime > 2000) {
            Toaster.show(R.string.app_HomeActivity_exit)
            exitTime = currentTimeMillis
        } else {
            finishAll()
        }
    }

    companion object {
        fun actionStart(context: Context, asUrl: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("asUrl", asUrl)
            context.startActivity(intent)
        }
    }
}
