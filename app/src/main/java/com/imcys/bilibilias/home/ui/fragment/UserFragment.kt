package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.databinding.FragmentUserBinding
import com.imcys.bilibilias.home.ui.adapter.UserDataAdapter
import com.imcys.bilibilias.home.ui.adapter.UserWorksAdapter
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.utils.HttpUtils
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlin.math.ceil


class UserFragment : Fragment() {


    private lateinit var userWorksAd: UserWorksAdapter
    private lateinit var userDataRvAd: UserDataAdapter
    private lateinit var userDataRv: RecyclerView
    private var userDataMutableList = mutableListOf<UserViewItemBean>()
    private lateinit var userWorksBean: UserWorksBean


    //
    lateinit var fragmentUserBinding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        initUserCardData()

        initUserWorks()

    }


    private fun loadUserWorks() {
        val oldMutableList = userWorksBean.data.list.vlist
        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.userWorksPath}?mid=${App.mid}&pn=${userWorksBean.data.page.pn + 1}&ps=20",
                UserWorksBean::class.java) {
                userWorksBean = it
                userWorksAd.submitList(oldMutableList + it.data.list.vlist)
            }
    }

    private fun initUserWorks() {
        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.userWorksPath}?mid=${App.mid}&qn=1&ps=20",
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

    private fun initUpState(userCardBean: UserCardBean) {
        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.userUpStat}?mid=${App.mid}", UpStatBeam::class.java) {
                userDataMutableList.add(UserViewItemBean(2,
                    upStatBeam = it,
                    userCardBean = userCardBean))
                userDataRvAd.submitList(userDataMutableList + mutableListOf())
            }
    }

    private fun initUserCardData() {
        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.getUserCardPath}?mid=${App.mid}", UserCardBean::class.java) {
                initUpState(it)
            }
    }

    private fun initUserData() {
        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.userBaseDataPath}?mid=${App.mid}", UserBaseBean::class.java) {
                userDataMutableList.add(UserViewItemBean(1, userBaseBean = it))
                userDataRvAd.submitList(userDataMutableList + mutableListOf())
            }


    }

    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }

    companion object {

        @JvmStatic
        fun newInstance() = UserFragment()
    }
}