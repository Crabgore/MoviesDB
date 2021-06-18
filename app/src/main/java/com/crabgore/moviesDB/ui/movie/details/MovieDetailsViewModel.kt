package com.crabgore.moviesDB.ui.movie.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.data.MovieDetailsResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.repositories.interfaces.FavoritesRepository
import com.crabgore.moviesDB.domain.repositories.interfaces.MoviesRepository
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MovieDetailsViewModel @Inject constructor(
    private val storage: Storage,
    private val repository: MoviesRepository,
    private val favoritesRepository: FavoritesRepository
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
            _movieState.value = repository.getMovieDetails(id)
            _castState.value = repository.castListStore
            _crewState.value = repository.crewListStore

            storage.getString(SESSION_ID)?.let {
                isInFavoritesLD.value = repository.getMovieAccountState(id)
            }
        }
        addJod(job)
    }

    fun markAsFavorite(id: Int) {
        val job = viewModelScope.launch {
            val success = favoritesRepository.markMovieAsFavorite(id, !isInFavoritesLD.value!!)
            if (success) isInFavoritesLD.value = !isInFavoritesLD.value!!
        }
        addJod(job)
    }
}