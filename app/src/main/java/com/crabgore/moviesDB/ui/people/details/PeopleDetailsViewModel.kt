package com.crabgore.moviesDB.ui.people.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.PeopleDetailsResponse
import com.crabgore.moviesDB.domain.remote.Remote
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

        addDisposable(detailsDisposable)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting people details/Movie Credits ${parseError(throwable)}")
    }

    private fun parseMovieDetailsResponse(response: PeopleDetailsResponse) {
        Timber.d("Got people Details $response")
        peopleLD.value = response

        //parse movie credits
        val movieCastList: MutableList<CreditsItem> = mutableListOf()
        val movieCrewList: MutableList<CreditsItem> = mutableListOf()
        (response.movieCredits?.cast?.sortedByDescending { it.releaseDate })?.forEach {
            movieCastList.add(CreditsItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult, character = it.character))
        }
        (response.movieCredits?.crew?.sortedByDescending { it.releaseDate })?.forEach {
            if (!it.isContains(movieCrewList))
                movieCrewList.add(CreditsItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult, job = it.job))
        }
        movieCastLD.value = movieCastList
        movieCrewLD.value = movieCrewList

        //parse tv credits
        val tvCastList: MutableList<CreditsItem> = mutableListOf()
        val tvCrewList: MutableList<CreditsItem> = mutableListOf()
        (response.tvCredits?.cast?.sortedByDescending { it.firstAirDate })?.forEach {
            tvCastList.add(CreditsItem(it.id, it.name, it.posterPath, it.voteAverage, null, character = it.character))
        }
        (response.tvCredits?.crew?.sortedByDescending { it.firstAirDate })?.forEach {
            if (!it.isContains(tvCrewList))
                tvCrewList.add(CreditsItem(it.id, it.name, it.posterPath, it.voteAverage, null, job = it.job))
        }
        tvCastLD.value = tvCastList
        tvCrewLD.value = tvCrewList

        increaseCounter()
    }
}