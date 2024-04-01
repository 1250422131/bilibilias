package com.imcys.bilibilias.privacy

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.hjq.toast.Toaster
import com.imcys.bilibilias.databinding.DialogLoginBottomsheetBinding
import com.imcys.bilibilias.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun PrivacyAgreementScreen(
    navigator: DestinationsNavigator
) {
    AndroidViewBinding(factory = { inflater, parent, attachToParent ->
        val binding = DialogLoginBottomsheetBinding.inflate(inflater, parent, attachToParent)
        binding.privacyBilibili.setOnClickListener { LoginViewModel.toBiliAgreement(it) }
        binding.privacyBilibilias.setOnClickListener { LoginViewModel.toBilibiliAsAgreement(it) }
        binding
    }) {
        dialogLoginBiliQr.setOnClickListener {
            navigator.navigate(LoginScreenDestination)
        }
        dialogLoginAs.setOnClickListener {
            Toaster.show("云端账户即将出炉")
        }
    }
}
