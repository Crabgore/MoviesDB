package com.crabgore.moviesDB.data.tv.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.data.tv.models.TVResponse
import com.crabgore.moviesDB.data.tv.services.TVService
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber

class TVRepository(
    private val api: TVService
) {
    var maxPages = Int.MAX_VALUE

    suspend fun getOnTheAirTVs(page: Int?): Resource<List<MovieItem>> {
        val response = api.onTheAirTVs(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    suspend fun getPopularTVs(page: Int?): Resource<List<MovieItem>> {
        val response = api.popularTVs(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    suspend fun getTopRatedTVs(page: Int?): Resource<List<MovieItem>> {
        val response = api.topRatedTVs(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    private fun parseResponse(response: Response<TVResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got TV response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
            }
            maxPages = response.body()?.totalPages!!
            Resource.Success(list)
        } else Resource.Error(data = null, message = response.message())
    }
}