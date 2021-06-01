package com.crabgore.moviesDB.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.MoviesResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MoviesViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    val nowPlayingLD: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val popularLD: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val topRatedLD: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val upcomingLD: MutableLiveData<List<MovieItem>> = MutableLiveData()

    fun getData() {
        Timber.d("Getting Now Playing Movies")
        val nowPlayingDisposable = remote.getNowPlayingMovies(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseNowPlayingResponse, ::handleFailure)


        Timber.d("Getting Popular Movies")
        val popularDisposable = remote.getPopularMovies(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parsePopularResponse, ::handleFailure)

        Timber.d("Getting Top Rated Movies")
        val topRatedDisposable = remote.getTopRatedMovies(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseTopRatedResponse, ::handleFailure)

        Timber.d("Getting Top Rated Movies")
        val upcomingDisposable = remote.getUpcomingMovies(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseUpcomingResponse, ::handleFailure)

        addDisposable(nowPlayingDisposable)
        addDisposable(popularDisposable)
        addDisposable(topRatedDisposable)
        addDisposable(upcomingDisposable)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting Movies ${parseError(throwable)}")
    }

    private fun parseNowPlayingResponse(response: MoviesResponse) {
        Timber.d("Got Now Playing $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        nowPlayingLD.value = list
        increaseCounter()
    }

    private fun parsePopularResponse(response: MoviesResponse) {
        Timber.d("Got Popular $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        popularLD.value = list
        increaseCounter()
    }

    private fun parseTopRatedResponse(response: MoviesResponse) {
        Timber.d("Got Top Rated $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        topRatedLD.value = list
        increaseCounter()
    }

    private fun parseUpcomingResponse(response: MoviesResponse) {
        Timber.d("Got Upcoming $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        upcomingLD.value = list
        increaseCounter()
    }
}