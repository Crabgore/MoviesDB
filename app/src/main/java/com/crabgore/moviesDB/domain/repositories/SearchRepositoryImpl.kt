package com.crabgore.moviesDB.domain.repositories

import android.content.Context
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.data.SearchMovieResponse
import com.crabgore.moviesDB.data.SearchPeopleResponse
import com.crabgore.moviesDB.data.SearchTVResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.repositories.interfaces.SearchRepository
import com.crabgore.moviesDB.ui.items.SearchItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val context: Context,
    private val remote: Remote
) : BaseRepository(), SearchRepository {

    override suspend fun getSearchMovieResults(query: String): Resource<List<SearchItem>> {
        val response = apiCall { remote.getSearchMovieResults(query) }
        return parseSearchMovieResponse(response)
    }

    override suspend fun getSearchTVResults(query: String): Resource<List<SearchItem>> {
        val response = apiCall { remote.getSearchTVResults(query) }
        return parseSearchTVResponse(response)
    }

    override suspend fun getSearchPeopleResults(query: String): Resource<List<SearchItem>> {
        val response = apiCall { remote.getSearchPeopleResults(query) }
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