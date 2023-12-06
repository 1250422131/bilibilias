package com.imcys.network.repository.user

import com.imcys.model.space.SpaceArcSearch

interface IUserDataSources {
    suspend fun getSpaceArcSearch(mid: Long, pageNumber: Int): SpaceArcSearch
}
