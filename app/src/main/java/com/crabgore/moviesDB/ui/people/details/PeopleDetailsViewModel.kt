package com.crabgore.moviesDB.ui.people.details

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.PeopleCreditsResponse
import com.crabgore.moviesDB.data.PeopleDetailsResponse
import com.crabgore.moviesDB.data.TVCastResponse
import com.crabgore.moviesDB.domain.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class PeopleDetailsViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    val peopleLD: MutableLiveData<PeopleDetailsResponse> = MutableLiveData()
    val movieCastLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()
    val movieCrewLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()
    val tvCastLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()
    val tvCrewLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()

    fun getData(id: Int) {
        Timber.d("Getting people Details $id")
        val detailsDisposable = remote.getPeopleDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseMovieDetailsResponse, ::handleFailure)

        Timber.d("Getting people Movie Credits")
        val movieCreditsDisposable = remote.getPeopleMovieCredits(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseMovieCreditsResponse, ::handleFailure)

        Timber.d("Getting people TV Credits")
        val tvCreditsDisposable = remote.getPeopleTVCredits(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseTVCreditsResponse, ::handleFailure)

        addDisposable(detailsDisposable)
        addDisposable(movieCreditsDisposable)
        addDisposable(tvCreditsDisposable)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting people details/Movie Credits ${parseError(throwable)}")
    }

    private fun parseMovieDetailsResponse(response: PeopleDetailsResponse) {
        Timber.d("Got people Details $response")
        peopleLD.value = response
    }

    private fun parseMovieCreditsResponse(response: PeopleCreditsResponse) {
        Timber.d("Got people Movie Credits $response")
        val castList: MutableList<CreditsItem> = mutableListOf()
        val crewList: MutableList<CreditsItem> = mutableListOf()
        (response.cast?.sortedByDescending { it.releaseDate })?.forEach {
            castList.add(CreditsItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult, character = it.character))
        }
        (response.crew?.sortedByDescending { it.releaseDate })?.forEach {
            if (!it.isContains(crewList))
                crewList.add(CreditsItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult, job = it.job))
        }

        movieCastLD.value = castList
        movieCrewLD.value = crewList
    }

    private fun parseTVCreditsResponse(response: TVCastResponse) {
        Timber.d("Got people Movie Credits $response")
        val castList: MutableList<CreditsItem> = mutableListOf()
        val crewList: MutableList<CreditsItem> = mutableListOf()
        (response.cast?.sortedByDescending { it.firstAirDate })?.forEach {
            castList.add(CreditsItem(it.id, it.name, it.posterPath, it.voteAverage, null, character = it.character))
        }
        (response.crew?.sortedByDescending { it.firstAirDate })?.forEach {
            if (!it.isContains(crewList))
                crewList.add(CreditsItem(it.id, it.name, it.posterPath, it.voteAverage, null, job = it.job))
        }

        tvCastLD.value = castList
        tvCrewLD.value = crewList
    }
}