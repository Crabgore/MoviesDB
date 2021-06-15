package com.crabgore.moviesDB.ui.tv.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.MediaTypes.Companion.TV
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
class TVDetailsViewModel @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseViewModel() {
    private val _tvState = MutableStateFlow<Resource<TVDetailsResponse>>(Resource.loading(null))
    private val _castState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _crewState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    val tvState = _tvState
    val castState = _castState
    val crewState = _crewState
    val isInFavoritesLD: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkSession(): String? {
        return storage.getString(SESSION_ID)
    }

    fun getData(id: Int) {
        val job = viewModelScope.launch {
            Timber.d("Getting TV Details $id")
            val detailsResponse = remote.getTvDetails(id)
            parseMovieDetailsResponse(detailsResponse)

            storage.getString(SESSION_ID)?.let {
                val accountStateResponse =
                    remote.getTVAccountState(id, storage.getString(SESSION_ID)!!)
                parseAccountState(accountStateResponse)
            }
        }
        addJod(job)
    }

    private fun parseMovieDetailsResponse(response: Response<TVDetailsResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got TV Details ${response.body()}")
            response.body()?.let {
                _tvState.value = Resource.success(it)


                val castList: MutableList<CreditsItem> = mutableListOf()
                val crewList: MutableList<CreditsItem> = mutableListOf()
                (it.credits?.cast?.sortedBy { tvCast -> tvCast.creditID })?.forEach { tvCast ->
                    castList.add(
                        CreditsItem(
                            tvCast.id,
                            tvCast.name,
                            tvCast.profilePath,
                            tvCast.popularity,
                            tvCast.adult,
                            character = tvCast.character
                        )
                    )
                }
                (it.credits?.crew?.sortedBy { tvCrew -> tvCrew.creditID })?.forEach { tvCrew ->
                    if (!tvCrew.isContains(crewList))
                        crewList.add(
                            CreditsItem(
                                tvCrew.id,
                                tvCrew.name,
                                tvCrew.profilePath,
                                tvCrew.popularity,
                                tvCrew.adult,
                                job = tvCrew.job
                            )
                        )
                }

                _castState.value = Resource.success(castList)
                _crewState.value = Resource.success(crewList)
            }
        } else _tvState.value = Resource.error(data = null, message = response.message())
    }

    private fun parseAccountState(response: Response<AccountStateResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got TV AccountState ${response.body()}")
            response.body()?.let {
                isInFavoritesLD.value = it.favorite
            }
        } else Timber.d("TV AccountState error ${response.message()}")
    }

    fun markAsFavorite(id: Int) {
        val request = MarkAsFavoriteRequest(TV, id, !isInFavoritesLD.value!!)

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