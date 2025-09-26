package com.imcys.bilibilias.ui.login.navigation

import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.database.entity.LoginPlatform
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute: NavKey

@Serializable
data class QRCodeLoginRoute(
    val defaultLoginPlatform: LoginPlatform = LoginPlatform.WEB,
    val isFromRoam: Boolean = false,
): NavKey
