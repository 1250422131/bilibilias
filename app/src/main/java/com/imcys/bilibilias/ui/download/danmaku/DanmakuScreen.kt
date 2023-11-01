package com.imcys.bilibilias.ui.download.danmaku

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DanmakuScreen(pages: List<com.imcys.model.VideoDetails.Page>, downloadDanmaku: (Long, Long) -> Unit, aid: Long) {
    Box {
        LazyColumn(Modifier.padding(bottom = ButtonDefaults.MinHeight)) {
            items(pages) { item ->
                Text(item.part, Modifier.clickable { downloadDanmaku(item.cid, aid) })
            }
        }
        Button(onClick = { /*TODO*/ }, Modifier.align(Alignment.BottomCenter)) {
            Text(text = "加入缓存")
        }
    }
}