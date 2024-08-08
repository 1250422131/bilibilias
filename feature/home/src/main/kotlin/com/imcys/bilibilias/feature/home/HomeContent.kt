package com.imcys.bilibilias.feature.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.load
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import dev.utils.app.AppUtils

@Composable
fun HomeContent(component: HomeComponent) {
    val model by component.models.collectAsStateWithLifecycle()
    HomeContent(model = model)
}

@Composable
internal fun HomeContent(
    model: HomeComponent.Model,
    onSalute: () -> Unit = {},
) {
    Scaffold { innerPadding ->
        val context = LocalContext.current
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    val lifecycleOwner = LocalLifecycleOwner.current
                    AndroidView(
                        factory = { context ->
                            Banner<String, BannerImageAdapter<String>>(context).apply {
                                addBannerLifecycleObserver(lifecycleOwner)
                                setIndicator(CircleIndicator(context))
                            }
                        },
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                    ) { banner ->
                        banner.setAdapter(
                            object : BannerImageAdapter<String>(model.homeBanner.imgUrlList) {
                                override fun onBindView(
                                    holder: BannerImageHolder,
                                    data: String,
                                    position: Int,
                                    size: Int,
                                ) {
                                    holder.imageView.load(data)
                                    holder.itemView.setOnClickListener {
                                        val uri = Uri.parse(model.homeBanner.dataList[position])
                                        val intent = Intent(Intent.ACTION_VIEW, uri)
                                        AppUtils.startActivity(intent)
                                    }
                                }
                            },
                        )
                    }
                }
                item {
                    HomeCard(
                        R.drawable.feature_home_ic_trophy,
                        onClick = onSalute,
                        title = "致敬",
                        desc = "爱好和追求不分年龄，无论何时，对生活有份热爱，才是最快乐的事，生命才能多姿多彩！—— BILIBILIAS用户",
                    )
                }
                item {
                    var show by remember { mutableStateOf(false) }
                    HomeCard(
                        R.drawable.feature_home_ic_savings,
                        onClick = { show = true },
                        title = "捐款",
                        desc = "BILIBILIAS的服务器会消耗费用，请我们一杯奶茶吧。",
                    )
                    if (show) {
                        Dialog(onDismissRequest = { show = false }) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = "https://view.misakamoe.com/donate/Alipay.jpg",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.weight(1f),
                                )
                                AsyncImage(
                                    model = "https://view.misakamoe.com/donate/WeChat.jpg",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
                item {
                    HomeCard(
                        R.drawable.feature_home_ic_cruelty_free,
                        onClick = {
                            startActivityForUri(
                                context,
                                "https://support.qq.com/product/337496",
                            )
                        },
                        title = "反馈问题",
                        desc = "如果您遇到了问题或者需要新增功能，就可以在社区反馈给我们。",
                    )
                }
            }
        }
    }
}

private fun startActivityForUri(context: Context, uri: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context.startActivity(intent)
}

@Composable
private fun HomeCard(resId: Int, title: String, desc: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp),
        ) {
            AsyncImage(
                resId,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = desc,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
