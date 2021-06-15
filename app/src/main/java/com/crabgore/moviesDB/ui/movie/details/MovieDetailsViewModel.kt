package com.crabgore.moviesDB.ui.movie.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.MediaTypes.Companion.MOVIE
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.*
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MovieDetailsViewModel @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseViewModel() {
    private val _movieState = MutableStateFlow<Resource<MovieDetailsResponse>>(Resource.loading(null))
    private val _castState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _crewState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    val movieState = _movieState
    val castState = _castState
    val crewState = _crewState
    val isInFavoritesLD: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkSession(): String? {
        return storage.getString(SESSION_ID)
    }

    fun getData(id: Int) {
        val job = viewModelScope.launch {
            Timber.d("Getting Movie Details $id")
            val detailsResponse = remote.getMovieDetails(id)
            parseMovieDetailsResponse(detailsResponse)

            storage.getString(SESSION_ID)?.let {
                val accountStateResponse = remote.getMovieAccountState(id, storage.getString(SESSION_ID)!!)
                parseAccountState(accountStateResponse)
            }
        }
        addJod(job)
    }

    private fun parseMovieDetailsResponse(response: Response<MovieDetailsResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got Movie Details ${response.body()}")
            response.body()?.let {
                _movieState.value = Resource.success(it)

                val castList: MutableList<CreditsItem> = mutableListOf()
                val crewList: MutableList<CreditsItem> = mutableListOf()
                (it.credits?.cast?.sortedBy { movieCast -> movieCast.creditID })?.forEach { movieCast ->
                    castList.add(
                        CreditsItem(
                            movieCast.id,
                            movieCast.name,
                            movieCast.profilePath,
                            movieCast.popularity,
                            movieCast.adult,
                            character = movieCast.character
                        )
                    )
                }
                (it.credits?.crew?.sortedBy { movieCrew -> movieCrew.creditID })?.forEach { movieCrew ->
                    if (!movieCrew.isContains(crewList))
                        crewList.add(
                            CreditsItem(
                                movieCrew.id,
                                movieCrew.name,
                                movieCrew.profilePath,
                                movieCrew.popularity,
                                movieCrew.adult,
                                job = movieCrew.job
                            )
                        )
                }

                _castState.value = Resource.success(castList)
                _crewState.value = Resource.success(crewList)
            }
        } else _movieState.value = Resource.error(data = null, message = response.message())
    }

    private fun parseAccountState(response: Response<AccountStateResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got Movie AccountState ${response.body()}")
            response.body()?.let {
                isInFavoritesLD.value = it.favorite
            }
        } else Timber.d("AccountState error ${response.message()}")
    }

    fun markAsFavorite(id: Int) {
        val request = MarkAsFavoriteRequest(MOVIE, id, !isInFavoritesLD.value!!)

        val job = viewModelScope.launch {
            val response = remote.markAsFavorite(storage.getInt(ACCOUNT_ID), storage.getString(SESSION_ID)!!, request)
            parseMarkAsFavoriteResponse(response)
        }
        addJod(job)
    }

    private fun parseMarkAsFavoriteResponse(response: Response<MarkAsFavoriteResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got MarkAsFavoriteResponse ${response.body()}")
            response.body()?.success?.let {
                if (it) isInFavoritesLD.value = !isInFavoritesLD.value!!
            }
        } else Timber.d("MarkAsFavoriteResponse error ${response.message()}")
    }
}