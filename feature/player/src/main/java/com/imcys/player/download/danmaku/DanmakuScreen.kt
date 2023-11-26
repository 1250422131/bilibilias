package com.imcys.player.download.danmaku

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
import com.imcys.model.video.PageData

@Composable
fun DanmakuScreen(pageData: List<PageData>, downloadDanmaku: (String, Long) -> Unit, aid: Long) {
    Box {
        LazyColumn(Modifier.padding(bottom = ButtonDefaults.MinHeight)) {
            items(pageData) { item ->
                Text(item.part, Modifier.clickable { downloadDanmaku(item.cid.toString(), aid) })
            }
        }
        Button(onClick = { /*TODO*/ }, Modifier.align(Alignment.BottomCenter)) {
            Text(text = "加入缓存")
        }
    }
}