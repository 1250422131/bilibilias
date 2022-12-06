package com.imcys.bilibilias.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.imcys.bilibilias.base.utils.asLogD
import com.imcys.bilibilias.utils.HttpUtils
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

open class BaseActivity : AppCompatActivity() {

    // 活动的类名
    private val TAG = javaClass.simpleName

    // 存储所有活动的列表
    private val activities = mutableListOf<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 添加当前活动
        addActivity(this)
        // 打印活动名称
        asLogD(this, TAG)
        // 沉浸式状态栏
        statusBarOnly(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 移除当前活动
        removeActivity(this)
    }

    // 沉浸式状态栏
    fun statusBarOnly(fragmentActivity: FragmentActivity) {
        UltimateBarX.statusBarOnly(fragmentActivity)
            .fitWindow(false)
            .light(true)
            .apply()
    }

    // 添加活动
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    // 移除活动
    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    // 结束所有活动
    fun finishAll() {
        activities.forEach {
            if (it.isFinishing) it.finish()
        }
    }

}
