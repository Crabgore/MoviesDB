package com.crabgore.moviesDB.ui.tv.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.TVResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVCategoryViewModel  @Inject constructor(
    private val context: Context,
    private val remote: Remote,
    private val storage: Storage
) : BaseViewModel() {
    val tvLiveData: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var maxPages = Int.MAX_VALUE

    fun getData(command: String, page: Int) {
        Timber.d("Getting TV category page:$page")
        if (page >= maxPages) {
            isLastPageLiveData.value = true
        } else {
            val disposable = when (command) {
                context.getString(R.string.on_the_air) -> remote.getOnTheAirTVs(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parseOnTheAirResponse, ::handleFailure)

                context.getString(R.string.popular) -> remote.getPopularTVs(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parsePopularResponse, ::handleFailure)

                context.getString(R.string.top_rating) -> remote.getTopRatedTVs(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parseTopRatedResponse, ::handleFailure)

                else -> remote.getFavoriteTVs(storage.getInt(ACCOUNT_ID), storage.getString(SESSION_ID)!!, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parseFavoriteTVResponse, ::handleFailure)
            }

            addDisposable(disposable)
        }
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting tv category ${parseError(throwable)}")
    }

    private fun parseOnTheAirResponse(response: TVResponse) {
        Timber.d("Got On The Air $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        tvLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }

    private fun parsePopularResponse(response: TVResponse) {
        Timber.d("Got Popular $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        tvLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }

    private fun parseTopRatedResponse(response: TVResponse) {
        Timber.d("Got Top Rated $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        tvLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }

    private fun parseFavoriteTVResponse(response: TVResponse) {
        Timber.d("Got favorites $response")
        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        tvLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }
}