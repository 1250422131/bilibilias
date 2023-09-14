package com.imcys.bilibilias.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.FullScreenScaffold
import com.imcys.bilibilias.common.base.components.NavigationCard
import com.imcys.bilibilias.home.ui.viewmodel.FragmentHomeViewModel
import com.zj.banner.BannerPager
import com.zj.banner.model.BaseBannerBean
import timber.log.Timber

@Composable
fun Home() {
    ScreenContent(Modifier.padding(horizontal = 20.dp))
}

data class BannerBean(
    override val data: String
) : BaseBannerBean()

@Composable
private fun ScreenContent(modifier: Modifier = Modifier) {
    val homeViewModel = hiltViewModel<FragmentHomeViewModel>()
    Timber.tag("viewmodel").d(System.identityHashCode(homeViewModel).toString())
    FullScreenScaffold(
        modifier
            .fillMaxSize(),
        topBar = {
            Text(
                stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            movableContentOf {
            }
            Banner()
            UpdateContent()
            Salute()
            // todo 捐款有特殊处理
            Donate()
            Feedback()
            Logout()
        }
    }
}

@Composable
private fun Logout() {
    NavigationCard(
        R.drawable.home_logout,
        stringResource(R.string.app_fragment_home_logout),
        stringResource(R.string.app_fragment_home_logout_text),
        ""
    )
}

@Composable
private fun Feedback(homeViewModel: FragmentHomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Timber.tag("viewmodel").d(System.identityHashCode(homeViewModel).toString())
    NavigationCard(
        R.drawable.ic_home_rabbit,
        stringResource(R.string.app_fragment_home_feedback),
        stringResource(R.string.app_fragment_home_feedback_text),
        stringResource(R.string.app_fragment_home_feedback_doc),
        Modifier.clickable { homeViewModel.goToCommunity(context) }
    )
}

@Composable
private fun Donate() {
    NavigationCard(
        R.drawable.ic_home_red_envelopes,
        stringResource(R.string.app_fragment_home_donate),
        stringResource(R.string.app_fragment_home_donate_text),
        stringResource(R.string.app_fragment_home_donate_doc)
    )
}

@Composable
private fun Salute(homeViewModel: FragmentHomeViewModel = hiltViewModel()) {
    Timber.tag("viewmodel").d(System.identityHashCode(homeViewModel).toString())
    NavigationCard(
        R.drawable.ic_home_trophy,
        stringResource(R.string.app_fragment_home_salute),
        stringResource(R.string.app_fragment_home_salute_text),
        stringResource(R.string.app_fragment_home_salute_doc),
    )
}

@Composable
private fun UpdateContent(homeViewModel: FragmentHomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Timber.tag("viewmodel").d(System.identityHashCode(homeViewModel).toString())
    NavigationCard(
        R.drawable.ic_tip_info,
        stringResource(R.string.app_fragment_home_update_content),
        stringResource(R.string.app_fragment_home_version),
        stringResource(R.string.new_version_doc),
        Modifier.clickable { homeViewModel.goToNewVersionDoc(context) }
    )
}

@Composable
fun Banner(modifier: Modifier = Modifier) {
    val items = arrayListOf(
        BannerBean("https://www.wanandroid.com/blogimgs/8a0131ac-05b7-4b6c-a8d0-f438678834ba.png"),
        BannerBean("https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png"),
        BannerBean("https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png"),
        BannerBean("https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png"),
    )
    BannerPager(
        modifier
            .height(180.dp)
            .fillMaxWidth(),
        items = items
    ) { item ->
    }
}
