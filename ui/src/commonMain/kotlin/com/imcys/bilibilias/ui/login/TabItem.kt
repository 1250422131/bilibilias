package com.imcys.bilibilias.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(val title: String, val icon: ImageVector) {
    companion object {
        fun valueOf() = listOf(
            TabItem("Cookie", Icons.Default.Cookie),
            TabItem("扫码", Icons.Default.QrCodeScanner),
        )
    }
}