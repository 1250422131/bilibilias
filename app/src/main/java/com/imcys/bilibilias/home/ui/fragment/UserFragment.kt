package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.TokenUtils
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.databinding.FragmentUserBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.adapter.UserDataAdapter
import com.imcys.bilibilias.home.ui.adapter.UserWorksAdapter
import com.imcys.bilibilias.home.ui.model.UpStatBeam
import com.imcys.bilibilias.home.ui.model.UserBaseBean
import com.imcys.bilibilias.home.ui.model.UserCardBean
import com.imcys.bilibilias.home.ui.model.UserViewItemBean
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.dkzwm.widget.srl.RefreshingListenerAdapter
import kotlin.math.ceil


class UserFragment : BaseFragment() {


    private lateinit var userWorksAd: UserWorksAdapter
    private lateinit var userDataRvAd: UserDataAdapter
    private lateinit var userDataRv: RecyclerView
    private var userDataMutableList = mutableListOf<UserViewItemBean>()
    private lateinit var userWorksBean: UserWorksBean


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


        checkDataRecovery(savedInstanceState)
        initView()



        return fragmentUserBinding.root
    }

    private fun checkDataRecovery(savedInstanceState: Bundle?) {

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
                        lifecycleScope.launch(Dispatchers.IO) {

                            //添加加密鉴权参数【此类方法将在下个版本被替换，因为我们需要让写法尽可能简单简短】
                            val params = mutableMapOf<String?, String?>()
                            params["mid"] = (context as HomeActivity).asUser.mid.toString()
                            params["pn"] = (userWorksBean.data.page.pn + 1).toString()
                            params["ps"] = "20"
                            val paramsStr = TokenUtils.getParamStr(params)

                            val userWorksBean =
                                KtHttpUtils.addHeader(
                                    "cookie",
                                    (context as HomeActivity).asUser.cookie
                                )
                                    .asyncGet<UserWorksBean>("${BilibiliApi.userWorksPath}?$paramsStr")
                            this@UserFragment.userWorksBean = userWorksBean

                            launch(Dispatchers.Main) {
                                userWorksAd.submitList(oldMutableList + userWorksBean.data.list.vlist)

                                //更新数据 -> fragmentUserWorksCsr 支持
                                refreshComplete()
                            }
                        }

                    } else {
                        //更新数据 -> fragmentUserWorksCsr 支持
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
        lifecycleScope.launch(Dispatchers.IO) {
            val userWorksBean =
                KtHttpUtils.addHeader(
                    "cookie",
                    BaseApplication.dataKv.decodeString("cookies", "")!!
                )
                    .asyncGet<UserWorksBean>("${BilibiliApi.userWorksPath}?mid=${(context as HomeActivity).asUser.mid}&pn=${userWorksBean.data.page.pn + 1}&ps=20")
            this@UserFragment.userWorksBean = userWorksBean

            launch(Dispatchers.Main) {
                userWorksAd.submitList(oldMutableList + userWorksBean.data.list.vlist)
            }
        }


    }

    private fun initUserWorks() {


        lifecycleScope.launch(Dispatchers.IO) {

            //添加加密鉴权参数【此类方法将在下个版本被替换，因为我们需要让写法尽可能简单简短】
            val params = mutableMapOf<String?, String?>()
            params["mid"] = (context as HomeActivity).asUser.mid.toString()
            params["qn"] = "1"
            params["ps"] = "20"
            val paramsStr = TokenUtils.getParamStr(params)

            val userWorksBean =
                KtHttpUtils.addHeader(
                    "cookie",
                    BaseApplication.dataKv.decodeString("cookies", "")!!
                )
                    .asyncGet<UserWorksBean>("${BilibiliApi.userWorksPath}?$paramsStr")

            userWorksAd = UserWorksAdapter()
            this@UserFragment.userWorksBean = userWorksBean

            if (userWorksBean.code == 0) {
                launch(Dispatchers.Main) {
                    //设置用户主页的作品的adapter
                    fragmentUserBinding.fragmentUserWorksRv.adapter = userWorksAd
                    //设置布局管理器，让作品呈瀑布流的形式展示。
                    fragmentUserBinding.fragmentUserWorksRv.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    //刷新刚刚请求的代码
                    userWorksAd.submitList(userWorksBean.data.list.vlist)
                }
            } else {

                launch(Dispatchers.Main) {
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

        //切到后台线程去
        lifecycleScope.launch(Dispatchers.IO) {

            userDataMutableList.clear()


            //获取基础内容
            val userBaseBean = async { getUserData() }

            // 用户卡片信息
            val userCardBean = async { getUserCardBean() }

            //获取up状态
            val userUpStat = async { getUpStat() }


            if (userBaseBean.await().code == 0) userDataMutableList.add(
                UserViewItemBean(
                    1,
                    userBaseBean = userBaseBean.await()
                )
            )


            launch(Dispatchers.Main) {
                userDataRvAd.submitList(userDataMutableList + mutableListOf())
            }


            if (userCardBean.await().code == 0 && userUpStat.await().code == 0) {
                userDataMutableList.add(
                    UserViewItemBean(
                        2,
                        upStatBeam = userUpStat.await(),
                        userCardBean = userCardBean.await()
                    )
                )
            }

            launch(Dispatchers.Main) {
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
        val params = mutableMapOf<String?, String?>()
        params["mid"] = (context as HomeActivity).asUser.mid.toString()
        val paramsStr = TokenUtils.getParamStr(params)

        return KtHttpUtils.addHeader("cookie", BaseApplication.dataKv.decodeString("cookies", "")!!)
            .asyncGet("${BilibiliApi.getUserCardPath}?$paramsStr")
    }

    /**
     * 获取用户状态信息
     * @return UpStatBeam
     */
    private suspend fun getUpStat(): UpStatBeam {
        return KtHttpUtils.addHeader("cookie", BaseApplication.dataKv.decodeString("cookies", "")!!)
            .asyncGet("${BilibiliApi.userUpStat}?mid=${(context as HomeActivity).asUser.mid}")
    }

    /**
     * 获取用户基础信息
     * @return UserBaseBean
     */
    private suspend fun getUserData(): UserBaseBean {
        val params = mutableMapOf<String?, String?>()
        params["mid"] = (context as HomeActivity).asUser.mid.toString()
        val paramsStr = TokenUtils.getParamStr(params)

        return KtHttpUtils.addHeader("cookie", BaseApplication.dataKv.decodeString("cookies", "")!!)
            .asyncGet("${BilibiliApi.userBaseDataPath}?$paramsStr")
    }


    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }


    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, "UserFragment")

    }

    //回收数据留存
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //保留当前页面的用户信息
        outState.putLong("mid", (context as HomeActivity).asUser.mid)
    }

    companion object {

        @JvmStatic
        fun newInstance() = UserFragment()
    }
}