package com.imcys.user

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

@Composable
fun User(userViewModel: UserViewModel) {
    val state by userViewModel.userDataState.collectAsState()
    Scaffold(
        Modifier.padding(horizontal = 20.dp),
        topBar = {
            Text(
                stringResource(R.string.app_fragment_user_title),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // region 用户头像，名称，个性签名
            System.identityHashCode("user1=$userViewModel")
            UserFaceItem(state)
            // endregion
            // region 粉丝，关注，获赞，播放
            UserCardDataItem(state)
            // endregion
            // region 工具栏
            UserTool()
            // endregion
        }
    }
}

@Composable
fun UserTool(userViewModel: UserViewModel = hiltViewModel()) {
    System.identityHashCode("user2=$userViewModel")
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
    ) {
        val navHostController = rememberNavController()
        UserToolColumn(
            R.drawable.ic_item_user_collection,
            R.string.app_item_fg_user_tool_favorites,
            Modifier.weight(1f)
        ) { navHostController.navigate("") }
        UserToolColumn(
            R.drawable.ic_item_user_play_history,
            R.string.app_item_fg_user_tool_play_history,
            Modifier.weight(1f)
        ) {}
        UserToolColumn(
            R.drawable.ic_item_bangumi_followr,
            R.string.app_item_fg_user_tool_follow_series,
            Modifier.weight(1f)
        ) {}
        UserToolColumn(
            R.drawable.ic_as_video_throw,
            R.string.app_item_fg_user_tool_coin_history,
            Modifier.weight(1f)
        ) {}
    }
}

@Composable
private fun UserToolColumn(@DrawableRes id: Int, strId: Int, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .clickable(onClick = onClick)
            .padding(10.dp),
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
private fun UserCardDataItem(state: UserState) {
    Row(Modifier.fillMaxWidth()) {
        UserCardDataColumn(state.fans, stringResource(R.string.app_item_fg_user_card_data_fans), Modifier.weight(1f))
        UserCardDataColumn(
            state.friend,
            stringResource(R.string.app_item_fg_user_card_data_friend),
            Modifier.weight(1f)
        )
        UserCardDataColumn(state.likes, stringResource(R.string.app_item_fg_user_card_data_liks), Modifier.weight(1f))
        UserCardDataColumn(state.archive, stringResource(R.string.app_item_fg_user_card_data_view), Modifier.weight(1f))
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
            color = MaterialTheme.colorScheme.tertiary,
            letterSpacing = TextUnit(0.05F, TextUnitType.Sp)
        )
    }
}

@Composable
private fun UserFaceItem(state: UserState) {
    Row(Modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = state.face,
                contentDescription = "avatar",
                Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(45.dp))
            )
        }
        Row(
            Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    state.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    letterSpacing = TextUnit(0.05f, TextUnitType.Sp),
                    color = state.nicknameColor
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    state.sign,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = Color.Black
                )
            }
        }
    }
}
