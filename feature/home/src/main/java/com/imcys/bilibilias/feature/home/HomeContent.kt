package com.imcys.bilibilias.feature.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.imcys.bilibilias.core.common.utils.ApkVerify
import com.imcys.bilibilias.core.designsystem.component.AsCard
import com.imcys.bilibilias.core.designsystem.component.AsModalBottomSheet
import com.imcys.bilibilias.core.model.bilibilias.Banner
import com.imcys.bilibilias.core.model.bilibilias.UpdateNotice
import com.imcys.bilibilias.core.network.api.BiliBiliAsApi
import dev.utils.app.AppUtils

@Composable
fun HomeContent(
    onSalute: () -> Unit,
    onDonation: () -> Unit,
    exitLogin: () -> Unit,
    banner: Banner,
    updateNotice: UpdateNotice,
    modifier: Modifier,
) {
    val context = LocalContext.current
    Column(modifier) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                com.imcys.bilibilias.core.ui.banner.Banner(
                    modifier = Modifier
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    count = banner.imgUrlList.size,
                    loop = true
                ) {
                    AsyncImage(
                        model = banner.imgUrlList[it],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clickable {
                            val uri = Uri.parse(banner.dataList[it])
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            AppUtils.startActivity(intent)
                        }
                    )
                }
            }
            item {
                HomeCard(
                    onClick = {
                        startActivityForUri(
                            context,
                            "https://docs.qq.com/doc/DVXZNWUVFakxEQ2Va"
                        )
                    },
                    title = "更新内容",
                    desc = ""
                )
            }
            item {
                HomeCard(
                    onClick = onSalute,
                    title = "致敬",
                    desc = "爱好和追求不分年龄，无论何时，对生活有份热爱，才是最快乐的事，生命才能多姿多彩！—— BILIBILIAS用户"
                )
            }
            item {
                HomeCard(
                    onClick = onDonation,
                    title = "捐款",
                    desc = "BILIBILIAS的服务器会消耗费用，请我们一杯奶茶吧。"
                )
            }
            item {
                HomeCard(
                    onClick = {
                        startActivityForUri(
                            context,
                            "https://support.qq.com/product/337496"
                        )
                    },
                    title = "反馈问题",
                    desc = "如果您遇到了问题或者需要新增功能，就可以在社区反馈给我们。"
                )
            }
            item {
                AsCard(
                    onClick = exitLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp)
                        )
                        Column(modifier = Modifier.padding(start = 20.dp)) {
                            Text(
                                text = "退出登录",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "退出账号登录",
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    DetectUpdateLogs(updateNotice)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetectUpdateLogs(
    updateNotice: UpdateNotice
) {
    val context = LocalContext.current
    var openUpdateWindow by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(Unit) {
        detectAppUpdate(updateNotice.version) {
            openUpdateWindow = true
        }
    }
    if (openUpdateWindow) {
        AsModalBottomSheet(
            onDismissRequest = { openUpdateWindow = false },
            sheetState,
            onConfirm = {
                startActivityForUri(context, updateNotice.url)
            },
            onCancel = {}
        ) {
            Text(text = updateNotice.gxNotice)
        }
    }
}

private fun detectAppUpdate(version: String, canUpdate: () -> Unit) {
    if (BiliBiliAsApi.VERSION.toString() != version) {
        canUpdate()
    }
}

private fun postAndCheckSignatureMessage(
    context: Context,
    postSignatureMessage: (String, Pair<String, Long>, String) -> Unit
) {
    val apkPath = context.packageCodePath
    val sha = ApkVerify.apkVerifyWithSHA(apkPath)
    val md5 = ApkVerify.apkVerifyWithMD5(apkPath)
    val crc = ApkVerify.apkVerifyWithCRC(apkPath)
    postSignatureMessage(sha, md5, crc)
}

private fun startActivityForUri(context: Context, uri: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context.startActivity(intent)
}

@Composable
private fun HomeCard(onClick: () -> Unit, title: String, desc: String) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            2.dp,
            Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp)
        ) {
            // TODO: 图标不对
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
            )
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = desc,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
