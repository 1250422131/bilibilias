package com.imcys.bilibilias.common.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.baidu.mobstat.StatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment(), BaseFragmentInit {
    protected val TAG = this::class.java.simpleName
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

    override fun initObserveViewModel() = Unit

    override fun onResume() {
        super.onResume()
        StatService.onPageStart(requireActivity(), TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, TAG)
    }
}
