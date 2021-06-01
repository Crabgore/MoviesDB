package com.crabgore.moviesDB.ui.tv.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.Const
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.*
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVDetailsViewModel @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseViewModel() {
    val TVLD: MutableLiveData<TVDetailsResponse> = MutableLiveData()
    val castLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()
    val crewLD: MutableLiveData<List<CreditsItem>> = MutableLiveData()
    val isInFavoritesLD: MutableLiveData<Boolean> = MutableLiveData()

    fun checkSession(): String? {
        return storage.getString(SESSION_ID)
    }

    fun getData(id: Int) {
        Timber.d("Getting TV Details $id")
        val detailsDisposable = remote.getTvDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseMovieDetailsResponse, ::handleFailure)

        storage.getString(SESSION_ID)?.let {
            Timber.d("Getting TV Account State $id")
            val accountStateDisposable =
                remote.getTVAccountState(id, it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parseAccountState, ::handleFailure)
            addDisposable(accountStateDisposable)
        }

        val creditsDisposable = remote.getTVCredits(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseMovieCreditsResponse, ::handleFailure)

        addDisposable(detailsDisposable)
        addDisposable(creditsDisposable)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting TV Details ${parseError(throwable)}")
    }

    private fun parseMovieDetailsResponse(response: TVDetailsResponse) {
        Timber.d("Got TV Details $response")
        TVLD.value = response
        increaseCounter()
    }

    private fun parseAccountState(response: AccountStateResponse) {
        Timber.d("Got Movie AccountState $response")
        isInFavoritesLD.value = response.favorite
    }

    private fun parseMovieCreditsResponse(response: MovieCreditsResponse) {
        Timber.d("Got TV Credits $response")
        val castList: MutableList<CreditsItem> = mutableListOf()
        val crewList: MutableList<CreditsItem> = mutableListOf()
        (response.cast?.sortedBy { it.creditID })?.forEach {
            castList.add(
                CreditsItem(
                    it.id,
                    it.name,
                    it.profilePath,
                    it.popularity,
                    it.adult,
                    character = it.character
                )
            )
        }
        (response.crew?.sortedBy { it.creditID })?.forEach {
            if (!it.isContains(crewList))
                crewList.add(
                    CreditsItem(
                        it.id,
                        it.name,
                        it.profilePath,
                        it.popularity,
                        it.adult,
                        job = it.job
                    )
                )
        }

        castLD.value = castList
        crewLD.value = crewList
        increaseCounter()
    }

    fun markAsFavorite(id: Int) {
        val request = MarkAsFavoriteRequest(Const.MediaTypes.TV, id, !isInFavoritesLD.value!!)

        val disposable = remote.markAsFavorite(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            request
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseMarkAsFavoriteResponse, ::handleFailure)

        addDisposable(disposable)
    }

    private fun parseMarkAsFavoriteResponse(response: MarkAsFavoriteResponse) {
        Timber.d("Got parseMarkAsFavoriteResponse $response")
        response.success?.let {
            if (it) isInFavoritesLD.value = !isInFavoritesLD.value!!
        }
    }
}