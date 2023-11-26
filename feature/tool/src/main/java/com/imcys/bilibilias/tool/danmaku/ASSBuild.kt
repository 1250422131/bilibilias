package com.imcys.bilibilias.tool.danmaku

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ASSBuild @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val assBaseInfo = IASSBaseInfo
    fun loadDanmaku(
        pool: List<Danmaku>,
        title: String,
        playResX: String,
        playResY: String,
    ): String {
        val scriptInfo = assBaseInfo.scriptInfo(title, playResX, playResY)
        val styles = assBaseInfo.styles()
        val events = assBaseInfo.events()

        val dialogue = generateDialogue(pool)
        val text = """
            $scriptInfo
            
            $styles
            
            $events
            
            $dialogue
        """.trimIndent()

        writeFile(text)
        return text
    }

    private fun writeFile(text: String) {
        val file = File(context.filesDir.path, "testDM.ass")
        file.writeText(text)
    }

    private fun generateDialogue(pool: List<Danmaku>): String {
        val factory = ASSDialogueFactory(pool)
        val sb = StringBuilder(2048)
        for (dialogue in factory) {
            sb.append(dialogue.dialogue())
        }
        return sb.toString()
    }
}
