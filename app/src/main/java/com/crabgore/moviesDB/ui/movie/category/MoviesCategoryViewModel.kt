package com.crabgore.moviesDB.ui.movie.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.MoviesResponse
import com.crabgore.moviesDB.domain.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MoviesCategoryViewModel @Inject constructor(
    private val context: Context,
    private val remote: Remote
) : BaseViewModel() {
    val moviesLiveData: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var maxPages = Int.MAX_VALUE

    fun getData(command: String, page: Int) {
        Timber.d("Getting Now Playing Movies page:$page")
        if (page >= maxPages) {
            isLastPageLiveData.value = true
        } else {
            val disposable = when (command) {
                context.getString(R.string.now_playing) -> remote.getNowPlayingMovies(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parseNowPlayingResponse, ::handleFailure)

                context.getString(R.string.popular) -> remote.getPopularMovies(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parsePopularResponse, ::handleFailure)

                context.getString(R.string.top_rating) -> remote.getTopRatedMovies(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parseTopRatedResponse, ::handleFailure)

                else -> remote.getUpcomingMovies(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parseUpcomingResponse, ::handleFailure)
            }

            addDisposable(disposable)
        }
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting Now Playing ${parseError(throwable)}")
    }

    private fun parseNowPlayingResponse(response: MoviesResponse) {
        Timber.d("Got Now Playing $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        moviesLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }

    private fun parsePopularResponse(response: MoviesResponse) {
        Timber.d("Got Popular $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        moviesLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }

    private fun parseTopRatedResponse(response: MoviesResponse) {
        Timber.d("Got Top Rated $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        moviesLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }

    private fun parseUpcomingResponse(response: MoviesResponse) {
        Timber.d("Got Upcoming $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        moviesLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }
}