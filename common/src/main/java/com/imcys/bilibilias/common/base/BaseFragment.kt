package com.imcys.bilibilias.common.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseFragment : Fragment(){
    fun launchIO(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        lifecycleScope.launch(Dispatchers.IO, start, block)
    }

     fun launchUI(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        lifecycleScope.launch(Dispatchers.Main, start, block)
    }

}