package com.imcys.bilibilias.weight

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.utils.ASConstant.PRIVACY_POLICY_URL
import com.imcys.bilibilias.common.utils.openLink

@Composable
fun ASAgreePrivacyPolicy(agreePrivacyPolicy: Boolean, onClick: () -> Unit) {
    val haptics = LocalHapticFeedback.current
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // 勾选同意隐私政策
        RadioButton(
            selected = agreePrivacyPolicy,
            onClick = {
                onClick()
                haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
            },
            modifier = Modifier
                .padding(0.dp)
                .scale(0.75f)
                .size(20.dp)
        )
        Text("我已阅读并同意", fontSize = 14.sp)
        Spacer(Modifier.width(4.dp))
        Text(
            "《BILIBILIAS 隐私政策》",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                context.openLink(PRIVACY_POLICY_URL)
            }
        )
    }
}