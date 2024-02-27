package com.imcys.bilibilias.home.ui.fragment

import android.os.*
import android.view.*
import android.widget.*
import androidx.databinding.*
import androidx.recyclerview.widget.*
import com.baidu.mobstat.*
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.*
import com.imcys.bilibilias.base.utils.*
import com.imcys.bilibilias.common.base.*
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.extend.*
import com.imcys.bilibilias.databinding.*
import com.imcys.bilibilias.home.ui.adapter.*
import com.imcys.bilibilias.home.ui.model.*
import com.zackratos.ultimatebarx.ultimatebarx.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import me.dkzwm.widget.srl.*
import javax.inject.*
import kotlin.math.*

@AndroidEntryPoint
class UserFragment : BaseFragment() {

    private lateinit var userWorksAd: UserWorksAdapter
    private lateinit var userDataRvAd: UserDataAdapter
    private lateinit var userDataRv: RecyclerView
    private var userDataMutableList = mutableListOf<UserViewItemBean>()
    private lateinit var userWorksBean: UserWorksBean

    lateinit var fragmentUserBinding: FragmentUserBinding

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var tokenUtils: TokenUtils
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

        initView()

        return fragmentUserBinding.root
    }

    private fun initView() {
        initUserDataRv()

        initUserData()
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
                            val paramsStr = tokenUtils.getParamStr(params)

                            val userWorksBean = networkService.n19(paramsStr)

                            this@UserFragment.userWorksBean = userWorksBean

                            launchUI {
                                userWorksAd.submitList(oldMutableList + userWorksBean.data.list.vlist)

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

    private fun initUserTool() {
        userDataMutableList.add(UserViewItemBean(3))
        userDataRvAd.submitList(userDataMutableList + mutableListOf())
    }

    private fun loadUserWorks() {
        val oldMutableList = userWorksBean.data.list.vlist
        launchIO {
            val userWorksBean = networkService.n20(userWorksBean.data.page.pn + 1)

            this@UserFragment.userWorksBean = userWorksBean

            launchUI {
                userWorksAd.submitList(oldMutableList + userWorksBean.data.list.vlist)
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
            val paramsStr = tokenUtils.getParamStr(params)

            val userWorksBean = networkService.n21(paramsStr)

            userWorksAd = UserWorksAdapter()
            this@UserFragment.userWorksBean = userWorksBean

            if (userWorksBean.code == 0) {
                launchUI {
                    // 设置用户主页的作品的adapter
                    fragmentUserBinding.fragmentUserWorksRv.adapter = userWorksAd
                    // 设置布局管理器，让作品呈瀑布流的形式展示。
                    fragmentUserBinding.fragmentUserWorksRv.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    // 刷新刚刚请求的代码
                    userWorksAd.submitList(userWorksBean.data.list.vlist)
                }
            } else {
                launchUI {
                    asToast(requireContext(), userWorksBean.message)
                }
            }
        }
    }

    private fun initUserDataRv() {
        userDataRv = fragmentUserBinding.fragmentUserDataRv
        userDataRvAd = UserDataAdapter()
        userDataRv.adapter = userDataRvAd
        userDataRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initUserData() {
        // 切到后台线程去
        launchIO {
            userDataMutableList.clear()

            // 获取基础内容
            val userBaseBean = async { getUserData() }

            // 用户卡片信息
            val userCardBean = getUserCardBean()

            // 获取up状态
            val userUpStat = async { networkService.getUpStat(userCardBean.data.card.mid) }

            if (userBaseBean.await().code == 0) {
                userDataMutableList.add(
                    UserViewItemBean(
                        1,
                        userBaseBean = userBaseBean.await(),
                    ),
                )
            }

            launchUI {
                userDataRvAd.submitList(userDataMutableList + mutableListOf())
            }

            if (userUpStat.await().code == 0) {
                userDataMutableList.add(
                    UserViewItemBean(
                        2,
                        upStatBeam = userUpStat.await(),
                        userCardBean = userCardBean,
                    ),
                )
            }

            launchUI {
                userDataRvAd.submitList(userDataMutableList + mutableListOf())
                initUserTool()
            }
        }
    }

    /**
     * 获取用户卡片信息
     * @return UserCardBean
     */
    private suspend fun getUserCardBean(): UserCardBean {
        val params = mutableMapOf<String, String>()
        params["mid"] = asUser.mid.toString()
        val paramsStr = tokenUtils.getParamStr(params)

        return networkService.n22(paramsStr)
    }

    /**
     * 获取用户基础信息
     * @return UserBaseBean
     */
    private suspend fun getUserData(): UserBaseBean {
        val params = mutableMapOf<String, String>()
        params["mid"] = asUser.mid.toString()
        val paramsStr = tokenUtils.getParamStr(params)

        return networkService.n24(paramsStr)
    }

    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
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
