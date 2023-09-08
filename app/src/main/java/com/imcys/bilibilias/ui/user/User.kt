package com.imcys.bilibilias.ui.user

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.FullScreenScaffold
import com.imcys.bilibilias.ui.theme.color_text_hint
import com.imcys.bilibilias.ui.theme.user_work_bg

@Composable
fun User() {
    val userViewModel = hiltViewModel<UserViewModel>()
    val state by userViewModel.userDataState.collectAsState()
    FullScreenScaffold(
        Modifier.padding(horizontal = 20.dp),
        topBar = {
            Text(
                stringResource(R.string.app_fragment_user_title),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding).padding(horizontal = 20.dp)) {
            // region item_fg_user_face
            UserFaceItem()
            // endregion
            // region item_fg_user_card_data
            UserCardDataItem()
            // endregion
            // region item_fg_user_tool
            UserTool()
            // endregion
        }
    }
}

@Composable
fun UserTool() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(user_work_bg, RoundedCornerShape(10.dp))
    ) {
        UserToolColumn(
            R.drawable.ic_item_user_collection,
            R.string.app_item_fg_user_tool_favorites,
            Modifier.weight(1f)
        )
        UserToolColumn(
            R.drawable.ic_item_user_play_history,
            R.string.app_item_fg_user_tool_play_history,
            Modifier.weight(1f)
        )
        UserToolColumn(
            R.drawable.ic_item_bangumi_followr,
            R.string.app_item_fg_user_tool_follow_series,
            Modifier.weight(1f)
        )
        UserToolColumn(R.drawable.ic_as_video_throw, R.string.app_item_fg_user_tool_coin_history, Modifier.weight(1f))
    }
}

@Composable
private fun UserToolColumn(@DrawableRes id: Int, strId: Int, modifier: Modifier) {
    Column(
        modifier
            .padding(10.dp)
            .clickable {},
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id),
            contentDescription = stringResource(strId),
            Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            stringResource(strId),
            Modifier.padding(top = 6.dp),
        )
    }
}

@Composable
private fun UserCardDataItem() {
    Row(Modifier.fillMaxWidth()) {
        UserCardDataColumn(100, stringResource(R.string.app_item_fg_user_card_data_fans), Modifier.weight(1f))
        UserCardDataColumn(100, stringResource(R.string.app_item_fg_user_card_data_friend), Modifier.weight(1f))
        UserCardDataColumn(200, stringResource(R.string.app_item_fg_user_card_data_liks), Modifier.weight(1f))
        UserCardDataColumn(100, stringResource(R.string.app_item_fg_user_card_data_view), Modifier.weight(1f))
    }
}

@Composable
private fun UserCardDataColumn(total: Int, title: String, modifier: Modifier) {
    Column(
        modifier.padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            total.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            title,
            Modifier.padding(top = 6.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color_text_hint,
            letterSpacing = TextUnit(0.05F, TextUnitType.Sp)
        )
    }
}

@Composable
private fun UserFaceItem() {
    Row(Modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = null,
                contentDescription = "avatar",
                Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(45.dp))
            )
        }
        Column(
            Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            // todo android:letterSpacing="0.05"
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    "name",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    "sign",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = Color.Black
                )
            }
        }
    }
}
