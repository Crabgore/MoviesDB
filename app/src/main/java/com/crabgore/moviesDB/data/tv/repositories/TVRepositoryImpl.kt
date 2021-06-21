package com.crabgore.moviesDB.data.tv.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.data.tv.models.TVResponse
import com.crabgore.moviesDB.data.tv.services.TVService
import com.crabgore.moviesDB.domain.repositories.interfaces.TVRepository
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class TVRepositoryImpl @Inject constructor(
    private val api: TVService
) : TVRepository {
    override var maxPages = Int.MAX_VALUE

    override suspend fun getOnTheAirTVs(page: Int?): Resource<List<MovieItem>> {
        val response = api.onTheAirTVs(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    override suspend fun getPopularTVs(page: Int?): Resource<List<MovieItem>> {
        val response = api.popularTVs(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    override suspend fun getTopRatedTVs(page: Int?): Resource<List<MovieItem>> {
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
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }
}