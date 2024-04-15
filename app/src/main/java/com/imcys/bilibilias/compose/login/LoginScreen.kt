package com.imcys.bilibilias.compose.login

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidViewBinding
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: LoginViewModel1 = getViewModel()
        val loginUiState by viewModel.loginUiState.collectAsState()
        AndroidViewBinding(
            factory = { inflater, parent, attachToParent ->
                val binding =
                    DialogLoginQrBottomsheetBinding.inflate(inflater, parent, attachToParent)
                binding.dialogLoginQrImage.setOnClickListener {
                    viewModel.applyQrCode(it.context)
                }
                binding.btnLogin.setOnClickListener {
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
}
