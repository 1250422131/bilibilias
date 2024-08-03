package com.imcys.bilias.feature.merge.di

import com.imcys.bilias.feature.merge.danmaku.DanmakuXMLParse
import com.imcys.bilias.feature.merge.danmaku.IDanmakuParse
import com.imcys.bilias.feature.merge.move.IMoveWorker
import com.imcys.bilias.feature.merge.move.MoveWorker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MergeModule {
    @Binds
    fun bindIDanmakuParse(danmakuXMLParse: DanmakuXMLParse): IDanmakuParse
    @Binds
    fun bindIMoveWorker(moveWorker: MoveWorker): IMoveWorker
}
