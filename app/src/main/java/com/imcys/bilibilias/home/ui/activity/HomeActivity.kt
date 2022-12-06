package com.imcys.bilibilias.home.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0
    private lateinit var activityHomeBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        loadFragment()


        getSavePermissions()

    }

    private fun getSavePermissions() {
        // 首先检查是否已经授予了储存权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                // 在这里可以弹出一个对话框来说明为什么需要此权限
            } else {
                // 申请储存权限
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            }
        } else {
            // 如果已经授予了储存权限，则可以进行相应的操作
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_WRITE_EXTERNAL_STORAGE -> {
                // 如果权限被授予，则可以进行相应的操作
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 已经授予了储存权限，可以进行相应的操作
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
                } else {
                    // 用户拒绝了权限请求，可以提醒用户为什么需要此权限
                }
                return
            }
        }
    }


    //加载fragment
    private fun loadFragment() {
        val fragmentArrayList = ArrayList<Fragment>()
        //添加fragment
        fragmentArrayList.add(HomeFragment.newInstance())
        fragmentArrayList.add(ToolFragment.newInstance())
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