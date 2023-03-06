package com.imcys.bilibilias.home.ui.activity.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.extend.digitalConversion
import com.imcys.bilibilias.home.ui.activity.ui.theme.BILIBILIASTheme
import com.imcys.bilibilias.home.ui.activity.ui.theme.ColorTextHint
import com.imcys.bilibilias.home.ui.activity.ui.theme.UserWorkBg
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import com.imcys.bilibilias.home.ui.model.view.UserIntent
import com.imcys.bilibilias.home.ui.model.view.UserViewModel


@Preview
@Composable
fun UserActivityLayout(
    viewModel: UserViewModel = UserViewModel(),
) {

    val viewState = viewModel.viewStates


    LaunchedEffect(Unit) {
        viewModel.userChannel.send(UserIntent.GetUserBaseBean)
        viewModel.userChannel.send(UserIntent.GetUserCardData)
    }


    BILIBILIASTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            fontSize = 20.sp,
                            text = "User",

                            )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    },
                    backgroundColor = Color.White,
                    elevation = 0.dp
                )
            },
        ) {
            BodySample(it, viewModel, viewState)
        }
    }

}


@Composable
fun BodySample(
    paddingValues: PaddingValues,
    viewModel: UserViewModel,
    viewState: UserViewModel.UserViewState,
) {


    Column() {

        Column(Modifier.padding(20.dp)) {

            FaceCardItem(viewModel, viewState)

            UserDataCardItem(viewModel, viewState)
        }

        UserWorkList(viewModel, viewState)


    }
}

/**
 * 头像部分item
 */
@Composable
fun FaceCardItem(
    viewModel: UserViewModel,
    viewState: UserViewModel.UserViewState,
) {


    Row {
        Surface(
            modifier = Modifier.size(90.dp),
            shape = RoundedCornerShape(45.dp)
        ) {
            AsyncImage(
                model = viewState.userBaseBean.data?.face,
                contentDescription = "头像",
                placeholder = painterResource(id = R.mipmap.ic_launcher),
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(modifier = Modifier
            .width(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                text = viewState.userBaseBean.data?.name ?: "用户名",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                text = viewState.userBaseBean.data?.sign ?: "签名",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun UserDataCardItem(viewModel: UserViewModel, viewState: UserViewModel.UserViewState) {


    Spacer(modifier = Modifier.height(10.dp))

    Row() {
        Column(
            modifier = Modifier
                .width(0.dp)
                .weight(1f, fill = true)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = (viewState.userCardBean?.data?.card?.fans ?: 0).digitalConversion()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                color = ColorTextHint,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                text = "粉丝"
            )

        }

        Column(
            modifier = Modifier
                .width(0.dp)
                .weight(1f, fill = true)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = (viewState.userCardBean?.data?.card?.friend ?: 0).digitalConversion()

            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                color = ColorTextHint,

                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                text = "关注"
            )

        }

        Column(
            modifier = Modifier
                .width(0.dp)
                .weight(1f, fill = true)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = (viewState.upStatBeam?.data?.likes ?: 0).digitalConversion()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                color = ColorTextHint,

                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                text = "获赞"
            )

        }


        Column(
            modifier = Modifier
                .width(0.dp)
                .weight(1f, fill = true)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = (viewState.upStatBeam?.data?.archive?.view ?: 0).digitalConversion()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                color = ColorTextHint,

                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                text = "播放"
            )

        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserWorkList(viewModel: UserViewModel, viewState: UserViewModel.UserViewState) {

    val workQn by remember {
        mutableStateOf(1)
    }

    LaunchedEffect(key1 = true) {
        viewModel.userChannel.send(UserIntent.GetUserWorksBean(workQn, 20))
    }

    Spacer(modifier = Modifier.height(20.dp))

    Surface(
        shape = RoundedCornerShape(
            topEndPercent = 5,
            topStartPercent = 5
        ),
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {


        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp, 20.dp),
            // item 和 item 之间的纵向间距
            verticalArrangement = Arrangement.spacedBy(20.dp),
            // item 和 item 之间的横向间距
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxHeight()
                .background(UserWorkBg),
        ) {

            item() {
                Text(
                    text = "作品",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.1.sp
                )
            }
            item {
                Spacer(modifier = Modifier.fillMaxWidth())
            }

            viewState.userWorksBean?.data?.list?.vlist?.forEach { vlistBean ->
                item(key = vlistBean.bvid) {
                    UserWorkSample(vlistBean)
                }
            }


        }


    }
}

@SuppressLint("InvalidColorHexValue")
@Composable
fun UserWorkSample(vlistBean: UserWorksBean.DataBean.ListBean.VlistBean) {

    Card(

        modifier = Modifier
            .fillMaxWidth(),
        elevation = 0.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(5.dp)

    ) {
        Column {

            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    model = vlistBean.pic,
                    contentDescription = "视频封面",
                    contentScale = ContentScale.FillBounds
                )

                Row(
                    Modifier
                        .padding(1.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color(0xFF80a1a3a6)),
                ) {


                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(painter = painterResource(id = R.drawable.ic_play_num),
                        contentDescription = "播放量图标",
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.CenterVertically),
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(text = vlistBean.play.digitalConversion(),
                        fontSize = 10.sp,
                        color = Color.White)

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(painter = painterResource(id = R.drawable.ic_danmaku_num),
                        contentDescription = "弹幕数图标",
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.CenterVertically),
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(text = vlistBean.video_review.digitalConversion(),
                        fontSize = 10.sp,
                        color = Color.White)

                }

            }

            Column(modifier = Modifier.padding(5.dp)) {

                Text(text = vlistBean.title, fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }

        }

    }

}