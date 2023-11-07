package com.imcys.bilibilias.home.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ActivityHomeBinding
import com.imcys.bilibilias.view.base.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFragment()
    }


    // 加载fragment
    private fun loadFragment() {
        binding.let {
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
}
