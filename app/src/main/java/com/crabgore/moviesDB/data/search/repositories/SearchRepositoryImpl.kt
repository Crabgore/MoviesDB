package com.crabgore.moviesDB.data.search.repositories

import android.content.Context
import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.data.search.models.SearchMovieResponse
import com.crabgore.moviesDB.data.search.models.SearchPeopleResponse
import com.crabgore.moviesDB.data.search.models.SearchTVResponse
import com.crabgore.moviesDB.data.search.services.SearchService
import com.crabgore.moviesDB.domain.repositories.interfaces.SearchRepository
import com.crabgore.moviesDB.ui.items.SearchItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val context: Context,
    private val api: SearchService
) : SearchRepository {

    override suspend fun getSearchMovieResults(query: String): Resource<List<SearchItem>> {
        val response = api.searchMovie(query, API_KEY, getLanguage())
        return parseSearchMovieResponse(response)
    }

    override suspend fun getSearchTVResults(query: String): Resource<List<SearchItem>> {
        val response =  api.searchTV(query, API_KEY, getLanguage())
        return parseSearchTVResponse(response)
    }

    override suspend fun getSearchPeopleResults(query: String): Resource<List<SearchItem>> {
        val response =  api.searchPeople(query, API_KEY, getLanguage())
        return parseSearchPeopleResponse(response)
    }

    private fun parseSearchMovieResponse(response: Response<SearchMovieResponse>): Resource<List<SearchItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got Search Movie Results ${response.body()}")
            val list: MutableList<SearchItem> = mutableListOf()
            response.body()?.let {
                it.results?.forEach { movie ->
                    list.add(
                        SearchItem(
                            context,
                            movie.id,
                            movie.posterPath,
                            movie.title,
                            movie.releaseDate,
                            movie.voteAverage
                        )
                    )
                }
            }
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }

    private fun parseSearchTVResponse(response: Response<SearchTVResponse>): Resource<List<SearchItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got Search TV Results ${response.body()}")
            val list: MutableList<SearchItem> = mutableListOf()
            response.body()?.let {
                it.results?.forEach { tv ->
                    list.add(
                        SearchItem(
                            context,
                            tv.id,
                            tv.posterPath,
                            tv.name,
                            tv.firstAirDate,
                            tv.voteAverage
                        )
                    )
                }
            }
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }

    private fun parseSearchPeopleResponse(response: Response<SearchPeopleResponse>): Resource<List<SearchItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got Search People Results ${response.body()}")
            val list: MutableList<SearchItem> = mutableListOf()
            response.body()?.let {
                it.results?.forEach { people ->
                    list.add(
                        SearchItem(
                            context,
                            people.id,
                            people.profilePath,
                            people.name,
                            null,
                            people.popularity
                        )
                    )
                }
            }
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }
}