package com.imcys.bilibilias.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.baidu.mobstat.StatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFragment<DB : ViewDataBinding> : Fragment(), BaseFragmentInit {
    protected val TAG: String = this.javaClass.simpleName
    private var _binding: DB? = null
    protected val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate<DB>(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        StatService.onPageStart(requireContext(), TAG)
    }

    override fun onResume() {
        super.onResume()
        StatService.onResume(requireActivity())
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(requireContext(), TAG)
        _binding = null
    }

    @Deprecated("不要在界面发请求了")
    fun launchIO(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        lifecycleScope.launch(Dispatchers.IO, start, block)
    }

    @Deprecated("不要在界面发请求了")
    fun launchUI(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        lifecycleScope.launch(Dispatchers.Main, start, block)
    }
}
