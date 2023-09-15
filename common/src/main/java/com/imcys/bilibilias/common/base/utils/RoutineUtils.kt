package com.imcys.bilibilias.common.base.utils

import android.content.Context
import android.widget.Toast
import timber.log.Timber

// 顶层方法
fun asLogI(tag: String, content: String) = Timber.tag(tag).i(content)

fun asLogD(tag: String, content: String) = Timber.tag(tag).d(content)

fun asLogE(tag: String, content: String) = Timber.tag(tag).e(content)

fun asLogV(tag: String, content: String) = Timber.tag(tag).v(content)

fun asLoW(tag: String, content: String) = Timber.tag(tag).w(content)

fun asLogI(context: Context, content: String) = Timber.tag(context::class.java.simpleName).i(content)

fun asLogD(context: Context, content: String) = Timber.tag(context::class.java.simpleName).d(content)

fun asLogE(context: Context, content: String) = Timber.tag(context::class.java.simpleName).e(content)

fun asLogV(context: Context, content: String) = Timber.tag(context::class.java.simpleName).v(content)

fun asLoW(context: Context, content: String) = Timber.tag(context::class.java.simpleName).w(content)

fun asToast(context: Context, content: String) =
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
