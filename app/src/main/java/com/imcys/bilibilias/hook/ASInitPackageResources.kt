package com.imcys.bilibilias.hook

import android.content.res.XModuleResources
import android.graphics.drawable.Drawable
import com.imcys.bilibilias.R
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_InitPackageResources


class ASInitPackageResources : IXposedHookInitPackageResources, IXposedHookZygoteInit {

    companion object {
        var modulePath: String? = null
        var logoDrawable: Drawable? = null

    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        val path = modulePath ?: return
        val modRes = XModuleResources.createInstance(path, resparam.res)

        logoDrawable = try {
            modRes.getDrawable(R.drawable.ic_logo_mini)
        } catch (e: Exception) {
            XposedBridge.log("加载模块图标失败: ${e.message}")
            null
        }
    }

}