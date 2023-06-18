package com.imcys.bilibilias.base

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.model.user.AsUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
open class BaseActivity : AbsActivity() {

    override val asSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        statusBarOnly(this)

        // 设置显示的语言
        setAppLanguage()
    }

    private fun setAppLanguage() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("app_language", "System") ?: "System"

        val configuration = Configuration(resources.configuration)
        val locale = when (language) {
            "System" -> {
                Locale.setDefault(Locale.getDefault())
                configuration.locale = Locale.getDefault()
                return
            }
            else -> Locale(language)
        }

        Locale.setDefault(locale)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    override fun attachBaseContext(base: Context) {
        val updatedContext = updateBaseContextLocale(base)
        super.attachBaseContext(updatedContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateBaseContextLocale(this)
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val language = sharedPreferences.getString("app_language", "System") ?: "System"
        val locale = when (language) {
            "System" -> Locale.getDefault()
            else -> Locale(language)
        }

        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }

}
