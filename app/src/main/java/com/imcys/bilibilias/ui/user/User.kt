package com.imcys.bilibilias.ui.user

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun User() {
    val userViewModel = hiltViewModel<UserViewModel>()
    val state by userViewModel.userDataState.collectAsState()
    Text(text = state)
}
