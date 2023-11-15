package com.imcys.network.repository.space

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imcys.model.space.SpaceArcSearch

class QueryUserSubmittedVideoPagingSource(
    private val spaceRepository: SpaceRepository,
    private val mid: Long
) : PagingSource<Int, SpaceArcSearch>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SpaceArcSearch> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val querySpaceArc = spaceRepository.querySpaceArc(mid, nextPageNumber)
            return LoadResult.Page(
                data = listOf(querySpaceArc),
                prevKey = null,
                nextKey = querySpaceArc.page.pn + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SpaceArcSearch>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
