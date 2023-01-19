package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.databinding.FragmentUserBinding
import com.imcys.bilibilias.home.ui.adapter.UserDataAdapter
import com.imcys.bilibilias.home.ui.adapter.UserWorksAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlinx.coroutines.*
import kotlin.math.ceil


class UserFragment : Fragment() {


    private lateinit var userWorksAd: UserWorksAdapter
    private lateinit var userDataRvAd: UserDataAdapter
    private lateinit var userDataRv: RecyclerView
    private var userDataMutableList = mutableListOf<UserViewItemBean>()
    private lateinit var userWorksBean: UserWorksBean


    //
    lateinit var fragmentUserBinding: FragmentUserBinding


    override fun onResume() {
        super.onResume()
        StatService.onPageStart(context, "UserFragment")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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


    }

    private fun initUserTool() {
        userDataMutableList.add(UserViewItemBean(3))
        userDataRvAd.submitList(userDataMutableList + mutableListOf())
    }


    private fun loadUserWorks() {
        val oldMutableList = userWorksBean.data.list.vlist
        HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .get("${BilibiliApi.userWorksPath}?mid=${BaseApplication.mid}&pn=${userWorksBean.data.page.pn + 1}&ps=20",
                UserWorksBean::class.java) {
                userWorksBean = it
                userWorksAd.submitList(oldMutableList + it.data.list.vlist)
            }
    }

    private fun initUserWorks() {



        HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .get("${BilibiliApi.userWorksPath}?mid=${BaseApplication.mid}&qn=1&ps=20",
                UserWorksBean::class.java) {
                userWorksBean = it
                userWorksAd = UserWorksAdapter()

                fragmentUserBinding.fragmentUserWorksRv.adapter = userWorksAd
                fragmentUserBinding.fragmentUserWorksRv.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                fragmentUserBinding.fragmentUserWorksRv.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (isSlideToBottom(recyclerView)) {
                            if (ceil((userWorksBean.data.page.count / 20).toDouble()) >= userWorksBean.data.page.pn + 1) {
                                loadUserWorks()
                            } else {
                                Toast.makeText(context, "真的到底部了", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }
                })

                userWorksAd.submitList(it.data.list.vlist)

            }

    }

    private fun initUserDataRv() {
        userDataRv = fragmentUserBinding.fragmentUserDataRv
        userDataRvAd = UserDataAdapter()
        userDataRv.adapter = userDataRvAd
        userDataRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun initUserData() {

        //切到后台线程去
        lifecycleScope.launch {

            userDataMutableList.clear()

            val userBaseBean = lifecycleScope.async { getUserData() }

            // 用户卡片信息
            val userCardBean = lifecycleScope.async { getUserCardBean() }

            //获取up状态
            val userUpStat = lifecycleScope.async { getUpStat() }


            userDataMutableList.add(UserViewItemBean(1, userBaseBean = userBaseBean.await()))

            userDataMutableList.add(
                UserViewItemBean(2,
                    upStatBeam = userUpStat.await(),
                    userCardBean = userCardBean.await())
            )

            userDataRvAd.submitList(userDataMutableList + mutableListOf())
            initUserTool()


        }


    }

    /**
     * 获取用户卡片信息
     * @return UserCardBean
     */
    private suspend fun getUserCardBean(): UserCardBean {
        return HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .asyncGet("${BilibiliApi.getUserCardPath}?mid=${BaseApplication.mid}",
                UserCardBean::class.java)
    }

    /**
     * 获取用户状态信息
     * @return UpStatBeam
     */
    private suspend fun getUpStat(): UpStatBeam {
        return HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .asyncGet("${BilibiliApi.userUpStat}?mid=${BaseApplication.mid}",
                UpStatBeam::class.java)
    }

    /**
     * 获取用户基础信息
     * @return UserBaseBean
     */
    private suspend fun getUserData(): UserBaseBean {
        return HttpUtils.addHeader("cookie", BaseApplication.cookies)
            .asyncGet("${BilibiliApi.userBaseDataPath}?mid=${BaseApplication.mid}",
                UserBaseBean::class.java)
    }

    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
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