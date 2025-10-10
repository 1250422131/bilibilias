package com.imcys.bilibilias.hook

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


class BILIBILIASHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam?.packageName != "") return
        // 执行Hook
        hook(lpparam)
    }

    private fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "",
            lpparam.classLoader,
            "onCreate",
            Bundle::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {}
                override fun afterHookedMethod(param: MethodHookParam) {
                    val act = param.thisObject as Activity
                    val pagerId = act.resources.getIdentifier("pager", "id", act.packageName)
                    val pager = act.findViewById<ViewGroup?>(pagerId)

                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            pager?.let { mPage ->
                                val rootViewGroup = mPage.getChildAt(0)
                                val recyclerId =
                                    act.resources.getIdentifier("recycler", "id", act.packageName)
                                val recyclerView =
                                    rootViewGroup.findViewById<ViewGroup?>(recyclerId)
                                recyclerView?.let { rv ->
                                    for (i in 0 until rv.childCount) {
                                        val item = rv.getChildAt(i)
                                        val dislikeIconId = act.resources.getIdentifier("dislike_icon", "id", act.packageName)
                                        val dislikeIcon = item.findViewById<ImageView?>(dislikeIconId)
                                        val dislikeNumId = act.resources.getIdentifier("dislike_num", "id", act.packageName)
                                        val dislikeNum = item.findViewById<TextView?>(dislikeNumId)
                                        dislikeIcon?.setImageDrawable(ASInitPackageResources.logoDrawable)

                                        dislikeNum?.text = "AS缓存"
                                    }
                                }
                            }

                            Toast.makeText(
                                act, "模块加载成功(35555)！", Toast.LENGTH_SHORT
                            ).show()
                        }, 2000L
                    )

                }
            })
    }
}


