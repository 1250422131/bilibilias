package com.imcys.bilibilias.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.FullScreenScaffold

@Composable
fun Home() {
    FullScreenScaffold(
        Modifier
            .padding(horizontal = 20.dp)
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
            NavigationCard(
                R.drawable.ic_tip_info,
                stringResource(R.string.app_fragment_home_update_content),
                stringResource(R.string.app_fragment_home_version),
                stringResource(R.string.new_version_doc)
            )
            NavigationCard(
                R.drawable.ic_home_trophy,
                stringResource(R.string.app_fragment_home_salute),
                stringResource(R.string.app_fragment_home_salute_text),
                stringResource(R.string.app_fragment_home_salute_doc)
            )
            // todo 捐款有特殊处理
            NavigationCard(
                R.drawable.ic_home_red_envelopes,
                stringResource(R.string.app_fragment_home_donate),
                stringResource(R.string.app_fragment_home_donate_text),
                stringResource(R.string.app_fragment_home_donate_doc)
            )
            NavigationCard(
                R.drawable.ic_home_rabbit,
                stringResource(R.string.app_fragment_home_feedback),
                stringResource(R.string.app_fragment_home_feedback_text),
                stringResource(R.string.app_fragment_home_feedback_doc)
            )
            NavigationCard(
                R.drawable.home_logout,
                stringResource(R.string.app_fragment_home_logout),
                stringResource(R.string.app_fragment_home_logout_text),
                ""
            )
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationCard(
    @DrawableRes iconId: Int,
    title: String,
    longTitle: String,
    doc: String,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = modifier.padding(top = 20.dp),
        border = BorderStroke(2.dp, Color(0XFFDEDEDE)),
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(iconId),
                    contentDescription = "icon",
                    Modifier.size(50.dp),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            }
            Column(Modifier.padding(start = 20.dp)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = longTitle, modifier = Modifier.padding(top = 5.dp), color = Color.Black)
                Text(
                    text = doc,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    Home()
}
