package com.imcys.bilibilias.compose.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.imcys.bilibilias.common.data.repository.RoamRepository
import com.imcys.bilibilias.databinding.DialogLoginQrBottomsheetBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

object LoginScreen:Screen {
    @Composable
    override fun Content() {
        AndroidViewBinding(DialogLoginQrBottomsheetBinding::inflate) {
            dialogLoginQrImage
            dialogLoginQrMessage
        }
    }
}
@HiltViewModel
class LoginViewModel@Inject constructor(private val loginRepository: LoginRepository):ViewModel(){

}