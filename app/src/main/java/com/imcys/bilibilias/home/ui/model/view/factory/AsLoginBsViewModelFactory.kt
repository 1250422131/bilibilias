package com.imcys.bilibilias.home.ui.model.view.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.databinding.DialogAsLoginBottomsheetBinding
import com.imcys.bilibilias.home.ui.model.view.AsLoginBsViewModel

class AsLoginBsViewModelFactory(
    private val asLoginBottomsheetBinding: DialogAsLoginBottomsheetBinding,
    private val bottomSheetDialog: BottomSheetDialog,
    private val finish: () -> Unit,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AsLoginBsViewModel(asLoginBottomsheetBinding, bottomSheetDialog, finish) as T
    }
}