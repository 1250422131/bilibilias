package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityHomeBinding
import com.imcys.bilibilias.home.ui.adapter.MyFragmentPageAdapter
import com.imcys.bilibilias.home.ui.fragment.DownloadFragment
import com.imcys.bilibilias.home.ui.fragment.HomeFragment
import com.imcys.bilibilias.home.ui.fragment.ToolFragment
import com.imcys.bilibilias.home.ui.fragment.UserFragment


class HomeActivity : BaseActivity() {
    private lateinit var activityHomeBinding: ActivityHomeBinding
    lateinit var toolFragment: ToolFragment
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


        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)


        loadFragment()


        parseShare()

    }

    //解析视频数据
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
                }
            }


        }
    }


    //加载fragment
    private fun loadFragment() {
        val fragmentArrayList = ArrayList<Fragment>()
        //添加fragment
        fragmentArrayList.add(HomeFragment.newInstance())
        toolFragment = ToolFragment.newInstance()
        fragmentArrayList.add(toolFragment)
        fragmentArrayList.add(DownloadFragment.newInstance())
        fragmentArrayList.add(UserFragment.newInstance())


        val myFragmentPageAdapter =
            MyFragmentPageAdapter(supportFragmentManager, lifecycle, fragmentArrayList)
        activityHomeBinding.let {
            it.homeViewPage.adapter = myFragmentPageAdapter
            it.homeViewPage.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                //滚动监听选择
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    it.homeBottomNavigationView.menu.getItem(position).isChecked = true
                }


            })

            it.homeViewPage.isUserInputEnabled = false


            //点击监听
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
}