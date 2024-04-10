package com.imcys.bilibilias.compose.login

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.ramcosta.composedestinations.annotation.Destination
import io.github.aakira.napier.Napier

@Destination
@Composable
fun LoginScreen(viewModel: LoginViewModel1 = hiltViewModel()) {
    val loginUiState by viewModel.loginUiState.collectAsState()
    AndroidViewBinding(
        factory = { inflater, parent, attachToParent ->
            val binding = DialogLoginQrBottomsheetBinding.inflate(inflater, parent, attachToParent)
            binding.dialogLoginQrImage.setOnClickListener {
                viewModel.applyQrCode(it.context)
            }
            binding.btnLogin.setOnClickListener {
                Napier.d { "登录状态 ${loginUiState.success}" }
                if (loginUiState.success) {
                    val intent = Intent(it.context, HomeActivity::class.java)
                    it.context.startActivity(intent)
                }
            }
            binding
        }
    ) {
        dialogLoginQrImage.setImageDrawable(genQrCode(loginUiState.qrCodeUrl))
        dialogLoginQrMessage.text = loginUiState.message
    }
}
