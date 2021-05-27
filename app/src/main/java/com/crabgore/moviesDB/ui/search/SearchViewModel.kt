package com.crabgore.moviesDB.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_MOVIE
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_TV
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.SearchMovieResponse
import com.crabgore.moviesDB.data.SearchPeopleResponse
import com.crabgore.moviesDB.data.SearchTVResponse
import com.crabgore.moviesDB.domain.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.SearchItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class SearchViewModel @Inject constructor(
    private val context: Context,
    private val remote: Remote
) : BaseViewModel() {
    val searchLD: MutableLiveData<List<SearchItem>> = MutableLiveData()

    fun search(searchId: String, query: String) {
        Timber.d("Getting Search Result")
        val disposable = when (searchId) {
            SEARCH_MOVIE -> remote.getSearchMovieResults(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(::onError)
                .subscribe(::parseSearchMovieResponse, ::handleFailure)
            SEARCH_TV -> remote.getSearchTVResults(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(::onError)
                .subscribe(::parseSearchTVResponse, ::handleFailure)
            else -> remote.getSearchPeopleResults(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(::onError)
                .subscribe(::parseSearchPeopleResponse, ::handleFailure)
        }

        addDisposable(disposable)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting Now Playing ${parseError(throwable)}")
    }

    private fun parseSearchMovieResponse(response: SearchMovieResponse) {
        Timber.d("Got Search Results $response")
        val list: MutableList<SearchItem> = mutableListOf()
        response.results?.forEach {
            list.add(SearchItem(context, it.id, it.posterPath, it.title, it.releaseDate, it.voteAverage))
        }
        searchLD.value = list
    }

    private fun parseSearchTVResponse(response: SearchTVResponse) {
        Timber.d("Got Search Results $response")
        val list: MutableList<SearchItem> = mutableListOf()
        response.results?.forEach {
            list.add(SearchItem(context, it.id, it.posterPath, it.name, it.firstAirDate, it.voteAverage))
        }
        searchLD.value = list
    }

    private fun parseSearchPeopleResponse(response: SearchPeopleResponse) {
        Timber.d("Got Search Results $response")
        val list: MutableList<SearchItem> = mutableListOf()
        response.results?.forEach {
            list.add(SearchItem(context, it.id, it.profilePath, it.name, null, it.popularity))
        }
        searchLD.value = list
    }
}