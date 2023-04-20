package com.imcys.bilibilias.home.ui.model.view.factory

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imcys.bilibilias.databinding.ActivityAsVideoBinding
import com.imcys.bilibilias.home.ui.model.view.AsVideoViewModel

class AsVideoViewModelFactory(
    @SuppressLint("StaticFieldLeak") val context: Context,
    private val asVideoBinding: ActivityAsVideoBinding,
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AsVideoViewModel(context, asVideoBinding) as T
    }
}