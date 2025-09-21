package com.imcys.bilibilias.common.update

import android.content.Context

abstract class ASAppUpdateManage {
    abstract suspend fun checkAppImmediateUpdate(): Boolean

    abstract suspend fun checkAppFlexibleUpdate(): Boolean
}