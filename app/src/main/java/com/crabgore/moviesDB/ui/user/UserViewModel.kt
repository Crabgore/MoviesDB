package com.crabgore.moviesDB.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.favorites.repositories.FavoritesRepository
import com.crabgore.moviesDB.data.user.models.AccountResponse
import com.crabgore.moviesDB.data.user.repositories.UserDetailsRepository
import com.crabgore.moviesDB.data.user.repositories.UserRepository
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class UserViewModel(
    private val userRepository: UserRepository,
    private val userDetailsRepository: UserDetailsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val storage: Storage
) : BaseViewModel() {
    private val _accountState = MutableStateFlow<Resource<AccountResponse>>(Resource.Loading(null))
    private val _favMoviesState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.Loading(null))
    private val _favTVState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.Loading(null))
    val accountState = _accountState
    val favMoviesState = _favMoviesState
    val favTVState = _favTVState

    val sessionIdLD: MutableLiveData<String> = MutableLiveData()
    val logoutLD: MutableLiveData<Boolean> = MutableLiveData()
    val loggingError: MutableLiveData<Boolean> = MutableLiveData()

    fun getSession(): String? {
        Timber.d("LOGING IN getting session ${storage.getString(SESSION_ID)}")
        return storage.getString(SESSION_ID)
    }

    fun login(username: String, password: String) {
        Timber.d("LOGING IN")
        val job = viewModelScope.launch {
            userRepository.login(username, password)?.let {
                sessionIdLD.value = it
            }
        }
        addJod(job)
    }

    fun logout() {
        Timber.d("Logging out")
        val job = viewModelScope.launch {
            if (userRepository.logout()) {
                sessionIdLD.value = null
                logoutLD.value = true
            }
        }
        addJod(job)
    }

    fun getData() {
        Timber.d("Getting account details api_key: $API_KEY account_id: ${storage.getInt(ACCOUNT_ID)} session_id: ${storage.getString(SESSION_ID)}")
        val job = viewModelScope.launch {
            _accountState.value = userDetailsRepository.getAccountDetails()
            _favMoviesState.value = favoritesRepository.getFavoriteMovies(null)
            _favTVState.value = favoritesRepository.getFavoriteTVs(null)
        }
        addJod(job)
    }
}