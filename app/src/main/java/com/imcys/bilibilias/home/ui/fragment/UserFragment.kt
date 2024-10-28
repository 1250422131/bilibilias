package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.TokenUtils
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.databinding.FragmentUserBinding
import com.imcys.bilibilias.home.ui.activity.user.UserVideoDownloadActivity
import com.imcys.bilibilias.home.ui.adapter.UserDataAdapter
import com.imcys.bilibilias.home.ui.adapter.UserWorksAdapter
import com.imcys.bilibilias.home.ui.model.UpStatBeam
import com.imcys.bilibilias.home.ui.model.UserBaseBean
import com.imcys.bilibilias.home.ui.model.UserCardBean
import com.imcys.bilibilias.home.ui.model.UserViewItemBean
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import me.dkzwm.widget.srl.RefreshingListenerAdapter
import javax.inject.Inject
import kotlin.math.ceil

@AndroidEntryPoint
class UserFragment : BaseFragment() {

    private lateinit var userWorksAd: UserWorksAdapter
    private lateinit var userDataRvAd: UserDataAdapter
    private lateinit var userDataRv: RecyclerView
    private var userDataMutableList = mutableListOf<UserViewItemBean>()
    private lateinit var userWorksBean: UserWorksBean
    private val userWorkList = mutableListOf<UserWorksBean.DataBean.ListBean.VlistBean>()
    private var mid: Long = asUser.mid


    lateinit var fragmentUserBinding: FragmentUserBinding

    @Inject
    lateinit var networkService: NetworkService

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

        // 刷新登录数据
        mid = asUser.mid
        this.arguments?.apply {
            mid = getLong("mid")
        }

        initView()


        return fragmentUserBinding.root
    }

    private fun initView() {
        initUserDataRv()

        initUserData()
        initUserWorks()

        initSmoothRefreshLayout()

        initUserVideoDownloadButton()


    }

    private fun initUserVideoDownloadButton() {
        // 批量视频下载按钮
        fragmentUserBinding.uvDownloadImage.setOnClickListener {
            UserVideoDownloadActivity.actionStart(requireContext(), mid)
        }

    }

    private fun initSmoothRefreshLayout() {
        fragmentUserBinding.fragmentUserWorksCsr.apply {
            setOnRefreshListener(object : RefreshingListenerAdapter() {
                override fun onLoadingMore() {
                    if (ceil((userWorksBean.data.page.count / 20).toDouble()) >= userWorksBean.data.page.pn) {
                        launchIO {
                            val userWorksBean = networkService.getUserWorkData(mid, userWorksBean.data.page.pn + 1)

                            this@UserFragment.userWorksBean = userWorksBean

                            launchUI {
                                userWorkList.addAll(userWorksBean.data.list.vlist)
                                userWorksAd.submitList(userWorkList + mutableListOf())
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

    private fun initUserWorks() {
        launchIO {
            val userWorksBean = networkService.getUserWorkData(mid,1)

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
                    userWorkList.addAll(userWorksBean.data.list.vlist)
                    userWorksAd.submitList(userWorkList + mutableListOf())
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
        launchUI {
            userDataMutableList.clear()

            // 获取基础内容
            val userBaseBean = async { getUserData() }

            // 用户卡片信息
            val userCardBean = async { getUserCardBean() }

            // 获取up状态
            val userUpStat = async { getUpStat() }

            if (userBaseBean.await().code == 0) {
                userDataMutableList.add(
                    UserViewItemBean(
                        1,
                        userBaseBean = userBaseBean.await(),
                    ),
                )
            }

            userDataRvAd.submitList(userDataMutableList + mutableListOf())


            if (userCardBean.await().code == 0 && userUpStat.await().code == 0) {
                userDataMutableList.add(
                    UserViewItemBean(
                        2,
                        upStatBeam = userUpStat.await(),
                        userCardBean = userCardBean.await(),
                    ),
                )
            }

            userDataRvAd.submitList(userDataMutableList + mutableListOf())

            if (userBaseBean.await().data.mid == asUser.mid) {
                initUserTool()
            }

        }
    }

    /**
     * 获取用户卡片信息
     * @return UserCardBean
     */
    private suspend fun getUserCardBean(): UserCardBean {
        return networkService.getUserCardData(mid)
    }

    /**
     * 获取用户状态信息
     * @return UpStatBeam
     */
    private suspend fun getUpStat(): UpStatBeam {

        return networkService.getUpStateInfo()
    }

    /**
     * 获取用户基础信息
     * @return UserBaseBean
     */
    private suspend fun getUserData(): UserBaseBean {
        return networkService.n11(mid)
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, "UserFragment")
    }

    // 回收数据留存
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 保留当前页面的用户信息
        outState.putLong("mid", mid)
    }

    companion object {

        @JvmStatic
        fun newInstance() = UserFragment()
    }
}
