package com.imcys.bilibilias.ui.user.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.model.user.BILIUserFolderDetailInfo
import kotlinx.coroutines.flow.last

class FolderFavPagingSource(
    val userInfoRepository: UserInfoRepository,
    val mediaId: Long
) : PagingSource<Int, BILIUserFolderDetailInfo.Media>() {
    override fun getRefreshKey(state: PagingState<Int, BILIUserFolderDetailInfo.Media>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BILIUserFolderDetailInfo.Media> {
        val page = params.key ?: 1
        if (mediaId <= 0L) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
        userInfoRepository.getFolderFavList(mediaId, pn = page, ps = 40).last()
            .let { result ->
                return when (result.status) {
                    ApiStatus.SUCCESS -> {
                        LoadResult.Page(
                            data = result.data?.medias ?: emptyList(),
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (result.data?.hasMore == true) page + 1 else null
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