package com.crabgore.moviesDB.ui.tv

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.TVResponse
import com.crabgore.moviesDB.domain.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    val nowPlayingLD: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val popularLD: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val topRatedLD: MutableLiveData<List<MovieItem>> = MutableLiveData()

    fun getData() {
        Timber.d("Getting On The Air TVs")
        val nowPlayingDisposable = remote.getOnTheAirTVs(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseOnTheAirResponse, ::handleFailure)


        Timber.d("Getting Popular TVs")
        val popularDisposable = remote.getPopularTVs(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parsePopularResponse, ::handleFailure)

        Timber.d("Getting Top Rated TVs")
        val topRatedDisposable = remote.getTopRatedTVs(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseTopRatedResponse, ::handleFailure)

        addDisposable(nowPlayingDisposable)
        addDisposable(popularDisposable)
        addDisposable(topRatedDisposable)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting TVs ${parseError(throwable)}")
    }

    private fun parseOnTheAirResponse(response: TVResponse) {
        Timber.d("Got Now Playing $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        nowPlayingLD.value = list
    }

    private fun parsePopularResponse(response: TVResponse) {
        Timber.d("Got Popular $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        popularLD.value = list
    }

    private fun parseTopRatedResponse(response: TVResponse) {
        Timber.d("Got Top Rated $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        topRatedLD.value = list
    }
}