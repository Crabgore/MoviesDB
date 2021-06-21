package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.ui.items.MovieItem

interface TVRepository {

    var maxPages: Int
    suspend fun getOnTheAirTVs(page: Int?): Resource<List<MovieItem>>
    suspend fun getPopularTVs(page: Int?): Resource<List<MovieItem>>
    suspend fun getTopRatedTVs(page: Int?): Resource<List<MovieItem>>
}