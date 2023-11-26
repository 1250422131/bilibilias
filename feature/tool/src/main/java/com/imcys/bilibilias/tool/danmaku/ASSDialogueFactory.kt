package com.imcys.bilibilias.tool.danmaku

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ASSDialogueFactory @AssistedInject constructor(@Assisted private val danmakuPool: List<Danmaku>) :
    Iterator<IASSDialogue> {
    override fun hasNext(): Boolean {
        return iterator().hasNext()
    }

    override fun next(): IASSDialogue {
        return DialogueFormat(iterator().next())
    }

    private operator fun iterator(): Iterator<Danmaku> {
        return danmakuPool.iterator()
    }
}
