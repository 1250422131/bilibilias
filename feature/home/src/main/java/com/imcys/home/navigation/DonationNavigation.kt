package com.imcys.home.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.imcys.designsystem.component.CenterColumn
import com.imcys.home.R
import kotlinx.coroutines.launch

const val ROUTE_DONATION = "donation"
fun NavController.navigateToDonation() {
    navigate(ROUTE_DONATION)
}

fun NavGraphBuilder.donationScreen() = composable(ROUTE_DONATION) {
    DonationRoute()
}

@Composable
internal fun DonationRoute() {
    DonationScreen()
}

@Composable
internal fun DonationScreen() {
    var payment by remember { mutableStateOf(Payment.Alipay) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
    BottomSheetScaffold(
        sheetContent = {
            when (payment) {
                Payment.Alipay -> PaymentCodeScreen("https://view.misakamoe.com/donate/Alipay.jpg", "aliPay") {
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                }

                Payment.WeChat -> PaymentCodeScreen("https://view.misakamoe.com/donate/WeChat.jpg", "wechatPay") {
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                }
            }
        }, scaffoldState = scaffoldState, sheetPeekHeight = 0.dp
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(Modifier.fillMaxWidth()) {
                PayButton(
                    id = R.drawable.ic_item_donate_pay_wechat,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    color = Color(android.graphics.Color.parseColor("#00c923"))
                ) {
                    payment = Payment.WeChat
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
                PayButton(
                    id = R.drawable.ic_item_donate_pay_alipay,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    color = Color(android.graphics.Color.parseColor("#1976ff")),
                ) {
                    payment = Payment.Alipay
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
            }
            ProgressMonth()
            Card(Modifier.padding(8.dp)) {
                Text(
                    stringResource(R.string.app_donate_doc),
                    Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun ProgressMonth() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(text = "本月进度: 11.50/50")
        LinearProgressIndicator(progress = 0.5f, strokeCap = StrokeCap.Round, modifier = Modifier.fillMaxWidth(.9f))
    }
}

@Composable
private fun PayButton(
    @DrawableRes id: Int,
    modifier: Modifier,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier,
        colors = ButtonDefaults.buttonColors(color)
    ) {
        Image(
            painter = painterResource(id), contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White), modifier = Modifier
                .size(50.dp)
                .padding(10.dp)
        )
    }
}

@Composable
private fun PaymentCodeScreen(url: String, desc: String, action: () -> Unit) {
    CenterColumn(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
    ) {
        Text(text = "感谢捐款支持")
        AsyncImage(model = url, contentDescription = desc)
        Button(onClick = action) {
            Text(text = "好嘞")
        }
    }
}

enum class Payment {
    Alipay, WeChat
}