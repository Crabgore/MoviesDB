package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.data.TVDetailsResponse
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.crabgore.moviesDB.ui.items.MovieItem

interface TVRepository {

    var maxPages: Int
    var castListStore: Resource<List<CreditsItem>>
    var crewListStore: Resource<List<CreditsItem>>
    suspend fun getOnTheAirTVs(page: Int?): Resource<List<MovieItem>>
    suspend fun getPopularTVs(page: Int?): Resource<List<MovieItem>>
    suspend fun getTopRatedTVs(page: Int?): Resource<List<MovieItem>>
    suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>>
    suspend fun getTVDetails(id: Int): Resource<TVDetailsResponse>
    suspend fun getTVsAccountState(id: Int): Boolean
}