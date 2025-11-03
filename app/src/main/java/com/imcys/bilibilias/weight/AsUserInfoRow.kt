package com.imcys.bilibilias.weight


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.shimmer.shimmer


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AsUserInfoRow(
    modifier: Modifier,
    pageInfoState: NetWorkResult<BILIUserSpaceAccInfo?>,
    loadingState: Boolean = false
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialShapes.Circle.toShape(),
            modifier = Modifier.shimmer(
                visible = loadingState
            )
        ) {
            ASAsyncImage(
                pageInfoState.data?.face ?: "",
                contentDescription = stringResource(R.string.user_avatar_1),
                modifier = Modifier.size(64.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    pageInfoState.data?.name ?: stringResource(R.string.user_user),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .shimmer(
                            visible = pageInfoState.status == ApiStatus.LOADING
                        )
                )
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.shimmer(
                        visible = pageInfoState.status == ApiStatus.LOADING
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                        text = "LV ${pageInfoState.data?.level}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                pageInfoState.data?.sign ?: stringResource(R.string.user_text_3),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .animateContentSize()
                    .shimmer(
                        visible = pageInfoState.status == ApiStatus.LOADING
                    )
            )
        }
    }
}