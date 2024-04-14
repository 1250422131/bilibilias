package com.imcys.bilibilias.privacy

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import cafe.adriel.voyager.core.screen.Screen
import com.hjq.toast.Toaster
import com.imcys.bilibilias.databinding.DialogLoginBottomsheetBinding

class PrivacyAgreementScreen1 : Screen {
    @Composable
    override fun Content() {
        AndroidViewBinding(factory = { inflater, parent, attachToParent ->
            val binding = DialogLoginBottomsheetBinding.inflate(inflater, parent, attachToParent)
            binding.privacyBilibili.setOnClickListener { LoginViewModel.toBiliAgreement(it) }
            binding.privacyBilibilias.setOnClickListener { LoginViewModel.toBilibiliAsAgreement(it) }
            binding
        }) {
            dialogLoginBiliQr.setOnClickListener {
//            navigator.navigate(LoginScreenDestination)
            }
            dialogLoginAs.setOnClickListener {
                Toaster.show("云端账户即将出炉")
            }
        }
    }
}
