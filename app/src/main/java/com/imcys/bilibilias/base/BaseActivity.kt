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

    private val TAG = javaClass.simpleName

    private val activities = mutableListOf<Activity>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this)
        asLogD(this, TAG)
        //沉浸式
        statusBarOnly(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        removeActivity(this)
    }

    fun statusBarOnly(fragmentActivity: FragmentActivity) {
        UltimateBarX.statusBarOnly(fragmentActivity)
            .fitWindow(false)
            .light(true)
            .apply()
    }


    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finishAll() {
        activities.forEach {
            if (it.isFinishing) it.finish()
        }
    }


}