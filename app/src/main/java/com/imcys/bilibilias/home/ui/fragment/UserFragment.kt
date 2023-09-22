package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.WbiUtils
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.view.base.BaseFragment
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.databinding.FragmentUserBinding
import com.imcys.bilibilias.home.ui.model.UserWorksBean
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
                            params["mid"] = asUser.mid.toString()
                            params["pn"] = (userWorksBean.data.page.pn + 1).toString()
                            params["ps"] = "20"
                            val paramsStr = WbiUtils.getParamStr(listOf(), "", "")

                            val userWorksBean =
                                KtHttpUtils.addHeader(
                                    COOKIE,
                                    asUser.cookie,
                                )
                                    .asyncGet<UserWorksBean>("${BilibiliApi.userWorksPath}?$paramsStr")
                            this@UserFragment.userWorksBean = userWorksBean

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

    private fun loadUserWorks() {
        val oldMutableList = userWorksBean.data.list.vlist
        launchIO {
            val userWorksBean =
                KtHttpUtils.addHeader(
                    COOKIE,
                    BaseApplication.dataKv.decodeString(COOKIES, "")!!,
                )
                    .asyncGet<UserWorksBean>(
                        "${BilibiliApi.userWorksPath}?mid=${asUser.mid}&pn=${userWorksBean.data.page.pn + 1}&ps=20"
                    )
            this@UserFragment.userWorksBean = userWorksBean

            launchUI {
            }
        }
    }

    private fun initUserWorks() {
        launchIO {
            // 添加加密鉴权参数【此类方法将在下个版本被替换，因为我们需要让写法尽可能简单简短】
            val params = mutableMapOf<String, String>()
            params["mid"] = asUser.mid.toString()
            params["qn"] = "1"
            params["ps"] = "20"
            val paramsStr = "" /*TokenUtils.getParamStr(params,"","")*/

            val userWorksBean =
                KtHttpUtils.addHeader(
                    COOKIE,
                    BaseApplication.dataKv.decodeString(COOKIES, "")!!,
                )
                    .asyncGet<UserWorksBean>("${BilibiliApi.userWorksPath}?$paramsStr")

            this@UserFragment.userWorksBean = userWorksBean

            if (userWorksBean.code == 0) {
                launchUI {
                    // 设置用户主页的作品的adapter
                    // 设置布局管理器，让作品呈瀑布流的形式展示。
                    fragmentUserBinding.fragmentUserWorksRv.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    // 刷新刚刚请求的代码
                }
            } else {
                launchUI {
                    asToast(requireContext(), userWorksBean.message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, "UserFragment")
    }

    // 回收数据留存
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 保留当前页面的用户信息
        outState.putLong("mid", asUser.mid)
    }

    companion object {

        @JvmStatic
        fun newInstance() = UserFragment()
    }
}
