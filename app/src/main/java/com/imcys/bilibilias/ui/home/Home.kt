package com.imcys.bilibilias.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.FullScreenScaffold
import com.imcys.bilibilias.common.base.components.NavigationCard
import com.imcys.bilibilias.home.ui.viewmodel.FragmentHomeViewModel

@Composable
fun Home() {
    val fragmentHomeViewModel = hiltViewModel<FragmentHomeViewModel>()
    ScreenContent(Modifier.padding(horizontal = 20.dp))
}

@Composable
private fun ScreenContent(modifier: Modifier = Modifier) {
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
            Banner()
            TipInfo()
            HomeTrophy()
            // todo 捐款有特殊处理
            RedEnvelopes()
            Rabbit()
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
private fun Rabbit() {
    NavigationCard(
        R.drawable.ic_home_rabbit,
        stringResource(R.string.app_fragment_home_feedback),
        stringResource(R.string.app_fragment_home_feedback_text),
        stringResource(R.string.app_fragment_home_feedback_doc)
    )
}

@Composable
private fun RedEnvelopes() {
    NavigationCard(
        R.drawable.ic_home_red_envelopes,
        stringResource(R.string.app_fragment_home_donate),
        stringResource(R.string.app_fragment_home_donate_text),
        stringResource(R.string.app_fragment_home_donate_doc)
    )
}

@Composable
private fun HomeTrophy() {
    NavigationCard(
        R.drawable.ic_home_trophy,
        stringResource(R.string.app_fragment_home_salute),
        stringResource(R.string.app_fragment_home_salute_text),
        stringResource(R.string.app_fragment_home_salute_doc)
    )
}

@Composable
private fun TipInfo() {
    NavigationCard(
        R.drawable.ic_tip_info,
        stringResource(R.string.app_fragment_home_update_content),
        stringResource(R.string.app_fragment_home_version),
        stringResource(R.string.new_version_doc)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Banner(modifier: Modifier = Modifier) {
    Card(
        onClick = { /*TODO*/ },
        modifier
            .height(180.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.Red),
        shape = RoundedCornerShape(10.dp)
    ) {
    }
}
