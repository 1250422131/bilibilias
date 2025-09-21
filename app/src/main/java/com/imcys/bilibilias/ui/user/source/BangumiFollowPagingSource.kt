package com.imcys.bilibilias.ui.user.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.model.user.BILIUserBangumiFollowInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last

/**
 * 追番信息分页
 */
class BangumiFollowPagingSource(
    val userInfoRepository: UserInfoRepository,
    val mid: Long
) : PagingSource<Int, BILIUserBangumiFollowInfo.ItemData>() {
    override fun getRefreshKey(state: PagingState<Int, BILIUserBangumiFollowInfo.ItemData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BILIUserBangumiFollowInfo.ItemData> {
        val page = params.key ?: 1
        if (mid <= 0L) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
        userInfoRepository.getBangumiFollowInfo(mid, pn = page, ps = 20).last()
            .let { result ->
                return when (result.status) {
                    ApiStatus.SUCCESS -> {
                        LoadResult.Page(
                            data = result.data?.list ?: emptyList(),
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (result.data?.list.isNullOrEmpty()) null else page + 1
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