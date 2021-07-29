package com.crabgore.moviesDB.data.movies.repositories

import android.content.Context
import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.common.getRegion
import com.crabgore.moviesDB.data.movies.models.MoviesResponse
import com.crabgore.moviesDB.data.movies.services.MovieService
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber

class MoviesRepository(
    private val context: Context,
    private val api: MovieService
) {
    var maxPages = Int.MAX_VALUE

    suspend fun getNowPlayingMovies(page: Int?): Resource<List<MovieItem>> {
        val response =
            api.nowPlayingMovies(API_KEY, getLanguage(), page, getRegion(context))
        return parseResponse(response)
    }

    suspend fun getPopularMovies(page: Int?): Resource<List<MovieItem>> {
        val response = api.popularMovies(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    suspend fun getTopRatedMovies(page: Int?): Resource<List<MovieItem>> {
        val response = api.topRatedMovies(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    suspend fun getUpcomingMovies(page: Int?): Resource<List<MovieItem>> {
        val response = api.upcomingMovies(API_KEY, getLanguage(), page, getRegion(context))
        return parseResponse(response)
    }

    private fun parseResponse(response: Response<MoviesResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got Movie response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
            }
            maxPages = response.body()?.totalPages!!
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }
}