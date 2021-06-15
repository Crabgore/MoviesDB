package com.crabgore.moviesDB.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.data.*
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseViewModel() {
    private val _accountState = MutableStateFlow<Resource<AccountResponse>>(Resource.loading(null))
    private val _favMoviesState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    private val _favTVState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
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
            val tokenResponse = remote.getToken()
            val token = parseTokenResponse(tokenResponse)

            val authRequest = AuthWithLoginRequest(username, password, token!!)
            val authedTokenResponse = remote.authWithLogin(authRequest)
            val authedToken = parseTokenResponse(authedTokenResponse)

            val sessionRequest = RequestToken(authedToken!!)
            val sessionIDResponse = remote.sessionId(sessionRequest)
            parseSessionResponse(sessionIDResponse)
        }
        addJod(job)
    }

    private fun parseTokenResponse(response: Response<TokenResponse>): String? {
        if (response.isSuccessful) {
            Timber.d("LOGING IN PARSING TOKEN RESPONSE ${response.body()}")
            response.body()?.let {
                Timber.d("LOGING IN Got token ${it.success} ${it.requestToken}")
                it.success?.let { success ->
                    if (success) return it.requestToken
                }
            }
        } else Timber.d("PARSING TOKEN RESPONSE ERROR ${response.message()}")

        return null
    }

    private fun parseSessionResponse(response: Response<SessionResponse>) {
        if (response.isSuccessful) {
            Timber.d("LOGING IN PARSING SESSION RESPONSE ${response.body()}")
            response.body()?.let {
                Timber.d("LOGING IN Got session ${it.success} ${it.sessionID}")
                it.success?.let { success ->
                    if (success) {
                        storage.putString(SESSION_ID, it.sessionID)
                        sessionIdLD.value = it.sessionID
                    }
                }
            }
        } else Timber.d("PARSING SESSION RESPONSE ERROR ${response.message()}")
    }

    fun logout() {
        Timber.d("Logging out")
        val job = viewModelScope.launch {
            val request = LogoutRequest(storage.getString(SESSION_ID)!!)
            val response = remote.logOut(request)
            parseLogoutResponse(response)
        }
        addJod(job)
    }

    private fun parseLogoutResponse(response: Response<DeleteSessionResponse>) {
        if (response.isSuccessful) {
            Timber.d("LOGING OUT ${response.body()}")
            response.body()?.let {
                Timber.d("Logged out ${it.success}")
                it.success?.let { success ->
                    if (success) {
                        storage.putString(SESSION_ID, null)
                        sessionIdLD.value = null
                        logoutLD.value = true
                    }
                }
            }
        } else Timber.d("LOGING OUT ERROR ${response.message()}")
    }

    fun getData() {
        Timber.d("Getting account details api_key: $API_KEY account_id: ${storage.getInt(ACCOUNT_ID)} session_id: ${storage.getString(SESSION_ID)}")
        val job = viewModelScope.launch {
            val account = remote.getAccountDetails(storage.getString(SESSION_ID)!!)
            parseAccountResponse(account)

            val movies = remote.getFavoriteMovies(
                storage.getInt(ACCOUNT_ID),
                storage.getString(SESSION_ID)!!,
                null
            )
            parseFavoriteMoviesResponse(movies)

            val tvs = remote.getFavoriteTVs(
                storage.getInt(ACCOUNT_ID),
                storage.getString(SESSION_ID)!!,
                null
            )
            parseFavoriteTVResponse(tvs)
        }
        addJod(job)
    }

    private fun parseAccountResponse(response: Response<AccountResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got account ${response.body()}")
            response.body()?.let {
                storage.putInt(ACCOUNT_ID, it.id)
                accountState.value = Resource.success(it)
            }
        } else accountState.value = Resource.error(data = null, message = response.message())
    }

    private fun parseFavoriteMoviesResponse(response: Response<MoviesResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got favorite movies ${response.body()}")
            response.body()?.let {
                val list: MutableList<MovieItem> = mutableListOf()
                it.results.forEach { movie ->
                    list.add(
                        MovieItem(
                            movie.id,
                            movie.title,
                            movie.posterPath,
                            movie.voteAverage,
                            movie.adult
                        )
                    )
                }
                favMoviesState.value = Resource.success(list)
            }
        } else favMoviesState.value = Resource.error(data = null, message = response.message())
    }

    private fun parseFavoriteTVResponse(response: Response<TVResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got favorite tvs $response")
            response.body()?.let {
                val list: MutableList<MovieItem> = mutableListOf()
                it.results.forEach { tv ->
                    list.add(MovieItem(tv.id, tv.name, tv.posterPath, tv.voteAverage, false))
                }
                favTVState.value = Resource.success(list)
            }
        } else favTVState.value = Resource.error(data = null, message = response.message())
    }
}