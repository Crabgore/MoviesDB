package com.crabgore.moviesDB.ui.search

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_MOVIE
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_TV
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.data.SearchMovieResponse
import com.crabgore.moviesDB.data.SearchPeopleResponse
import com.crabgore.moviesDB.data.SearchTVResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.SearchItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class SearchViewModel @Inject constructor(
    private val context: Context,
    private val remote: Remote
) : BaseViewModel() {
    private val _searchState = MutableStateFlow<Resource<List<SearchItem>>>(Resource.loading(null))
    val searchState = _searchState

    fun search(searchId: String, query: String) {
        Timber.d("Getting Search Result")
        val job = when (searchId) {
            SEARCH_MOVIE -> viewModelScope.launch {
                val response = remote.getSearchMovieResults(query)
                parseSearchMovieResponse(response)
            }
            SEARCH_TV -> viewModelScope.launch {
                val response = remote.getSearchTVResults(query)
                parseSearchTVResponse(response)
            }
            else -> viewModelScope.launch {
                val response = remote.getSearchPeopleResults(query)
                parseSearchPeopleResponse(response)
            }
        }
        addJod(job)
    }

    private fun parseSearchMovieResponse(response: Response<SearchMovieResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got Search Movie Results ${response.body()}")
            response.body()?.let {
                val list: MutableList<SearchItem> = mutableListOf()
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
                searchState.value = Resource.success(list)
            }
        } else _searchState.value = Resource.error(data = null, message = response.message())
    }

    private fun parseSearchTVResponse(response: Response<SearchTVResponse>) {
        Timber.d("Got Search TV Results ${response.body()}")
        if (response.isSuccessful) {
            response.body()?.let {
                val list: MutableList<SearchItem> = mutableListOf()
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
                searchState.value = Resource.success(list)
            }
        } else _searchState.value = Resource.error(data = null, message = response.message())
    }

    private fun parseSearchPeopleResponse(response: Response<SearchPeopleResponse>) {
        Timber.d("Got Search People Results ${response.body()}")
        if (response.isSuccessful) {
            response.body()?.let {
                val list: MutableList<SearchItem> = mutableListOf()
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
                searchState.value = Resource.success(list)
            }
        } else _searchState.value = Resource.error(data = null, message = response.message())
    }
}