package com.imcys.network.repository.user

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imcys.model.space.SpaceArcSearch
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class GetUserSubmittedVideoPagingSource @AssistedInject constructor(
    private val userRepository: UserRepository,
    @Assisted private val mid: Long
) : PagingSource<Int, SpaceArcSearch.Lists.Vlist>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SpaceArcSearch.Lists.Vlist> {
        return try {
            val nextPageNumber = params.key ?: 1
            val querySpaceArc = userRepository.getSpaceArcSearch(mid, nextPageNumber)

            return LoadResult.Page(
                data = querySpaceArc.list.vlist,
                prevKey = null,
                nextKey = querySpaceArc.page.pn + 1
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
