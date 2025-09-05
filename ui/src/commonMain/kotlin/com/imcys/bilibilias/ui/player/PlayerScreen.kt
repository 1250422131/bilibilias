package com.imcys.bilibilias.ui.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.imcys.bilibilias.logic.player.PlayerViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.openani.mediamp.compose.MediampPlayerSurface
import org.openani.mediamp.compose.rememberMediampPlayer
import org.openani.mediamp.playUri

@Composable
fun PlayerScreen(playerViewModel: PlayerViewModel = koinViewModel()) {
    VideoSurface()
}

@Composable
fun VideoSurface() {
    val player = rememberMediampPlayer()
    val scope = rememberCoroutineScope()
    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Button(onClick = {
                scope.launch {
                    player.playUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4")
                }
            }) {
                Text("Play")
            }

            MediampPlayerSurface(player, Modifier.fillMaxSize())
        }
    }
}