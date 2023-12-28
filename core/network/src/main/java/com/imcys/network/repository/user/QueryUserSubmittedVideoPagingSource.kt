package com.imcys.network.repository.user

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imcys.model.space.SpaceArcSearch

class GetUserSubmittedVideoPagingSource constructor(
    private val userRepository: IUserDataSources,
    private val mid: Long
) : PagingSource<Int, SpaceArcSearch.Lists.Vlist>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SpaceArcSearch.Lists.Vlist> {
        return try {
            val nextPageNumber = params.key ?: 1
            val querySpaceArc = userRepository.getSpaceArcSearch(mid, nextPageNumber)

            val vlist = querySpaceArc.list.vlist
            return LoadResult.Page(
                data = vlist,
                prevKey = null,
                nextKey = if (vlist.isEmpty()) null else querySpaceArc.page.pageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SpaceArcSearch.Lists.Vlist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
