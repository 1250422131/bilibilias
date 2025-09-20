package com.imcys.bilibilias.ui.user.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imcys.bilibilias.data.model.user.BILIUserHistoryPlayModel
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.network.ApiStatus
import kotlinx.coroutines.flow.last

class HistoryPlayPagingSource(
    val userInfoRepository: UserInfoRepository,
): PagingSource<HistoryPlayPagingSource.Cursor, BILIUserHistoryPlayModel>() {

    data class Cursor(
        val viewAt: Long = 0L,
        val max: Long = 0L
    )

    override fun getRefreshKey(state: PagingState<Cursor, BILIUserHistoryPlayModel>): Cursor? {
        // 直接返回最近的 prevKey 或 nextKey，或返回 null
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Cursor>): LoadResult<Cursor, BILIUserHistoryPlayModel> {
        val currentCursor = params.key ?: Cursor()
        userInfoRepository.getHistoryCursor(max = currentCursor.max, viewAt = currentCursor.viewAt).last()
            .let { result ->
                return when (result.status) {
                    ApiStatus.SUCCESS -> {
                        val data = result.data ?: emptyList()
                        // 取最后一个元素的游标作为下一页的游标
                        val nextCursor = data.lastOrNull()?.let {
                            Cursor(viewAt = it.viewAt, max = it.max)
                        }
                        val nextKey = if (data.isEmpty() || nextCursor == null || nextCursor == currentCursor) {
                            null
                        } else {
                            nextCursor
                        }

                        LoadResult.Page(
                            data = data,
                            prevKey = null, // 不支持向前加载历史
                            nextKey = nextKey
                        )
                    }

                    ApiStatus.ERROR -> {
                        LoadResult.Error(Throwable(result.errorMsg ?: ""))
                    }
                    ApiStatus.LOADING -> {
                        LoadResult.Error(Throwable("Loading"))
                    }
                    ApiStatus.DEFAULT -> {
                        LoadResult.Error(Throwable("Default"))
                    }
                }
            }

    }


}