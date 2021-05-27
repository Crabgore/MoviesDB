package com.crabgore.moviesDB.ui.movie.details

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.MovieCreditsResponse
import com.crabgore.moviesDB.data.PeopleCreditsResponse
import com.crabgore.moviesDB.data.MovieDetailsResponse
import com.crabgore.moviesDB.domain.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MovieDetailsViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    val movieLD: MutableLiveData<MovieDetailsResponse> = MutableLiveData()
    val castLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()
    val crewLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()

    fun getData(id: Int) {
        Timber.d("Getting Movie Details $id")
        val detailsDisposable = remote.getMovieDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseMovieDetailsResponse, ::handleFailure)

        Timber.d("Getting Movie Credits")
        val creditsDisposable = remote.getMovieCredits(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseMovieCreditsResponse, ::handleFailure)

        addDisposable(detailsDisposable)
        addDisposable(creditsDisposable)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting Movie Details ${parseError(throwable)}")
    }

    private fun parseMovieDetailsResponse(response: MovieDetailsResponse) {
        Timber.d("Got Movie Details $response")
        movieLD.value = response
    }

    private fun parseMovieCreditsResponse(response: MovieCreditsResponse) {
        Timber.d("Got Movie Credits $response")
        val castList: MutableList<CreditsItem> = mutableListOf()
        val crewList: MutableList<CreditsItem> = mutableListOf()
        (response.cast?.sortedBy { it.creditID })?.forEach {
            castList.add(CreditsItem(it.id, it.name, it.profilePath, it.popularity, it.adult, character = it.character))
        }
        (response.crew?.sortedBy { it.creditID })?.forEach {
            if (!it.isContains(crewList))
                crewList.add(CreditsItem(it.id, it.name, it.profilePath, it.popularity, it.adult, job = it.job))
        }

        castLD.value = castList
        crewLD.value = crewList
    }
}