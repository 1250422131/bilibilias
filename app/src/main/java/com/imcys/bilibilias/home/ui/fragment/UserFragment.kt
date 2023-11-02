package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.databinding.FragmentUserBinding
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import com.imcys.bilibilias.view.base.BaseFragment
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import me.dkzwm.widget.srl.RefreshingListenerAdapter
import kotlin.math.ceil

class UserFragment : BaseFragment() {
    private lateinit var userWorksBean: UserWorksBean

    lateinit var fragmentUserBinding: FragmentUserBinding

    override fun onResume() {
        super.onResume()
        StatService.onPageStart(context, "UserFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentUserBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)

        fragmentUserBinding.fragmentUserTopLinearLayout.addStatusBarTopPadding()
        return fragmentUserBinding.root
    }
     override fun initView() {
        initUserWorks()

        initSmoothRefreshLayout()
    }

    private fun initSmoothRefreshLayout() {
        fragmentUserBinding.fragmentUserWorksCsr.apply {
            setOnRefreshListener(object : RefreshingListenerAdapter() {
                override fun onLoadingMore() {
                    if (ceil((userWorksBean.data.page.count / 20).toDouble()) >= userWorksBean.data.page.pn + 1) {
                        val oldMutableList = userWorksBean.data.list.vlist
                        launchIO {
                            // 添加加密鉴权参数【此类方法将在下个版本被替换，因为我们需要让写法尽可能简单简短】
                            val params = mutableMapOf<String, String>()

                            params["pn"] = (userWorksBean.data.page.pn + 1).toString()
                            params["ps"] = "20"
                            launchUI {

                                // 更新数据 -> fragmentUserWorksCsr 支持
                                refreshComplete()
                            }
                        }
                    } else {
                        // 更新数据 -> fragmentUserWorksCsr 支持
                        refreshComplete()
                        Toast.makeText(context, "真的到底部了", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun initUserWorks() {
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, "UserFragment")
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserFragment()
    }
}
