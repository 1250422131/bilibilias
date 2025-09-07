package com.imcys.bilibilias.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.imcys.bilibilias.logic.login.CookieAction
import com.imcys.bilibilias.logic.login.CookieLoginState
import com.imcys.bilibilias.logic.login.QrCodeLoginAction
import com.imcys.bilibilias.logic.login.QrCodeLoginState
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import io.github.alexzhirkevich.qrose.toByteArray
import kotlinx.coroutines.launch

@Composable
internal fun PagerScope.QrContent(
    state: QrCodeLoginState,
    dispatch: (QrCodeLoginAction) -> Unit,
    onBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    when (state) {
        is QrCodeLoginState.Error -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(state.errorMessage)
            }
        }

        QrCodeLoginState.LoginSuccess -> {
            onBack()
        }

        is QrCodeLoginState.QRCodeReady -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("正在生成二维码。。。")
            }
        }

        is QrCodeLoginState.Content ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val painter = rememberQrCodePainter(data = state.url)
                Text(
                    text = "使用 bilibili 官方 App 扫码登录",
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "剩余有效时间: ${state.lifeTime} 秒",
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.clickable { dispatch(QrCodeLoginAction.Generate) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh QR Code",
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "刷新二维码", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(32.dp))
                    Row(
                        modifier = Modifier.clickable {
                            dispatch(
                                QrCodeLoginAction.SaveToAlbum(
                                    painter
                                        .toImageBitmapWithBorder(
                                            1024,
                                            1024,
                                            200f
                                        )
                                        .toByteArray()
                                )
                            )
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SaveAlt,
                            contentDescription = "Save to Album",
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "保存至相册", fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.height(30.dp))
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(150.dp).clickable(null, null) {
                        dispatch(QrCodeLoginAction.Generate)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = state.message, fontSize = 16.sp)
                @Suppress("DEPRECATION")
                val clipboard = LocalClipboardManager.current
                val scope = rememberCoroutineScope()

                Text(
                    text = state.url,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .clickable {
                            scope.launch {
                                clipboard.setText(AnnotatedString(state.url))
                                onShowSnackbar(
                                    "已复制到剪贴板，可粘贴至已登录的app私信处发送，然后点击已发送的链接打开",
                                    null
                                )
                            }
                        },
                    color = MaterialTheme.colorScheme.onSurface.copy(0.4f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "请务必在 BilibiliAs 开源仓库等可信渠道下载安装。",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

        else -> {}
    }
}

@Composable
internal fun PagerScope.CookieContent(
    cookieLoginState: CookieLoginState,
    dispatch: (CookieAction) -> Unit,
    onBack: () -> Unit,
) {
    SideEffect {
        if (cookieLoginState.success) {
            onBack()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "使用Cookie登录",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = cookieLoginState.text,
            onValueChange = { dispatch((CookieAction.Changed(it))) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cookie") },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Cookie,
                    contentDescription = "Cookie Icon"
                )
            },
            trailingIcon = {
                if (cookieLoginState.text.isNotEmpty()) {
                    IconButton(onClick = { dispatch(CookieAction.Changed("")) }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear text"
                        )
                    }
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedButton(
            onClick = {
                if (cookieLoginState.text.isNotBlank()) {
                    dispatch(CookieAction.TryLogin)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(180.dp),
            shape = RoundedCornerShape(50)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Login Arrow"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "登录", fontSize = 16.sp)
            }
        }
    }
}

/**
 * Converts a Painter to an ImageBitmap, adding a white border around the image.
 *
 * @param painter The Painter to convert.
 * @param outputWidth The desired width of the output ImageBitmap (including the border).
 * @param outputHeight The desired height of the output ImageBitmap (including the border).
 * @param borderWidth The width of the white border in pixels.
 * @param density The current screen density.
 * @param layoutDirection The current layout direction.
 * @return An ImageBitmap with the original painter content and a white border.
 */
fun Painter.toImageBitmapWithBorder(
    outputWidth: Int,
    outputHeight: Int,
    borderWidth: Float,
): ImageBitmap {
    // Create an ImageBitmap with the specified output dimensions
    val imageBitmap = ImageBitmap(outputWidth, outputHeight)
    val canvas = Canvas(imageBitmap)

    // Calculate the dimensions of the inner image (without the border)
    val imageContentWidth = outputWidth - (borderWidth * 2)
    val imageContentHeight = outputHeight - (borderWidth * 2)

    CanvasDrawScope().draw(
        density = Density(1f, 1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = Size(outputWidth.toFloat(), outputHeight.toFloat())
    ) {
        // Draw the white background (border)
        drawRect(
            color = Color.White,
            size = Size(outputWidth.toFloat(), outputHeight.toFloat())
        )

        // Draw the original painter content inside the border
        translate(left = borderWidth, top = borderWidth) {
            draw(size = Size(imageContentWidth, imageContentHeight))
        }
    }
    return imageBitmap
}