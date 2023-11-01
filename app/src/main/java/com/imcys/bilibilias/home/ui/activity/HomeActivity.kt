package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.databinding.ActivityHomeBinding
import com.imcys.bilibilias.home.ui.adapter.MyFragmentPageAdapter
import com.imcys.bilibilias.home.ui.fragment.DownloadFragment
import com.imcys.bilibilias.home.ui.fragment.HomeFragment
import com.imcys.bilibilias.home.ui.fragment.ToolFragment
import com.imcys.bilibilias.home.ui.fragment.UserFragment
import com.imcys.bilibilias.view.base.BaseActivity
import com.xiaojinzi.component.Component

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private var exitTime: Long = 0

    lateinit var toolFragment: ToolFragment
    lateinit var homeFragment: HomeFragment
    lateinit var downloadFragment: DownloadFragment
    lateinit var userFragment: UserFragment
    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Component.inject(target = this)
        loadFragment()
    }

    /**
     * 初始化fragment
     */
    override fun initView() {
        homeFragment = HomeFragment.newInstance()
        toolFragment = ToolFragment.newInstance()
        userFragment = UserFragment.newInstance()
        downloadFragment = DownloadFragment.newInstance()
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

    companion object {
        fun actionStart(context: Context, asUrl: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("asUrl", asUrl)
            context.startActivity(intent)
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
