package com.imcys.bilibilias.core.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imcys.bilibilias.core.model.space.SpaceArcSearch
import com.imcys.bilibilias.core.model.space.SpaceArcSearch.VList
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.network.repository.UserSpaceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SpaceArcSearchPagingSource @AssistedInject constructor(
    private val userSpaceRepository: UserSpaceRepository,
    @Assisted private val mid: Mid,
) : PagingSource<Int, SpaceArcSearch.VList.Vlist>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VList.Vlist> {
        return try {
            val nextPageNumber = params.key ?: 1

            val response =
                userSpaceRepository.查询用户投稿视频(mid, nextPageNumber, params.loadSize)
            if (response.page.count == 0) {
                return LoadResult.Invalid()
            }
            LoadResult.Page(
                data = response.list.vlist,
                prevKey = null,
                nextKey = if (response.list.vlist.isNotEmpty()) nextPageNumber + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SpaceArcSearch.VList.Vlist>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(mid: Mid): SpaceArcSearchPagingSource
    }
}
