package com.crabgore.moviesDB.ui.tv.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.tv.models.TVDetailsResponse
import com.crabgore.moviesDB.domain.repositories.interfaces.FavoritesRepository
import com.crabgore.moviesDB.domain.repositories.interfaces.TVDetailsRepository
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVDetailsViewModel @Inject constructor(
    private val storage: Storage,
    private val tvDetailsRepository: TVDetailsRepository,
    private val favoritesRepository: FavoritesRepository
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
            _tvState.value = tvDetailsRepository.getTVDetails(id)
            _castState.value = tvDetailsRepository.castListStore
            _crewState.value = tvDetailsRepository.crewListStore

            storage.getString(SESSION_ID)?.let {
                isInFavoritesLD.value = favoritesRepository.getTVsAccountState(id)
            }
        }
        addJod(job)
    }

    fun markAsFavorite(id: Int) {
        val job = viewModelScope.launch {
            val success = favoritesRepository.markTVAsFavorite(id, !isInFavoritesLD.value!!)
            if (success) isInFavoritesLD.value = !isInFavoritesLD.value!!
        }
        addJod(job)
    }
}