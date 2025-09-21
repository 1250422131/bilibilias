package com.imcys.bilibilias.ui.event.playvoucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.datastore.source.UsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayVoucherErrorViewModel(
    private val usersDataSource: UsersDataSource,
) : ViewModel() {


    fun ontUseTVVoucherInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            usersDataSource.setNotUseBuvid3(true)
        }
    }


}