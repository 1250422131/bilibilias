package com.imcys.bilibilias.privacy

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hjq.toast.Toaster
import com.imcys.bilibilias.compose.login.LoginScreen
import com.imcys.bilibilias.databinding.DialogLoginBottomsheetBinding

class PrivacyAgreementScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AndroidViewBinding(factory = { inflater, parent, attachToParent ->
            val binding = DialogLoginBottomsheetBinding.inflate(inflater, parent, attachToParent)
            binding.privacyBilibili.setOnClickListener { LoginViewModel.toBiliAgreement(it) }
            binding.privacyBilibilias.setOnClickListener { LoginViewModel.toBilibiliAsAgreement(it) }
            binding
        }) {
            dialogLoginBiliQr.setOnClickListener {
                navigator.push(LoginScreen())
            }
            dialogLoginAs.setOnClickListener {
                Toaster.show("云端账户即将出炉")
            }
        }
    }
}
