package com.crabgore.moviesDB.ui.user

import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.*
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseViewModel() {
    val sessionIdLD: MutableLiveData<String> = MutableLiveData()
    val accountLD: MutableLiveData<AccountResponse> = MutableLiveData()
    val logoutLD: MutableLiveData<Boolean> = MutableLiveData()
    val loggingError: MutableLiveData<Boolean> = MutableLiveData()

    val favMoviesLD: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val favTVLD: MutableLiveData<List<MovieItem>> = MutableLiveData()

    fun getSession(): String? {
        return storage.getString(SESSION_ID)
    }

    fun login(username: String, password: String) {
        Timber.d("LOGING IN")
        val disposable = remote.getToken()
            .map(::parseTokenResponse)
            .flatMap { getAuthedRequestToken(it, username, password) }
            .map(::parseTokenResponse)
            .flatMap(::getSessionID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseSessionResponse, ::handleFailure)

        addDisposable(disposable)
    }

    private fun getAuthedRequestToken(
        token: String,
        username: String,
        password: String
    ): Single<TokenResponse> {
        Timber.d("LOGING IN getting authed token $token, $username, $password")
        val request = AuthWithLoginRequest(username, password, token)

        return remote.authWithLogin(request)
    }

    private fun getSessionID(token: String): Single<SessionResponse> {
        Timber.d("LOGING IN getting session ID $token")
        val request = RequestToken(token)

        return remote.sessionId(request)
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("LOGING IN Error ${parseError(throwable)}")
        loggingError.value = true
    }


    private fun parseTokenResponse(response: TokenResponse): String? {
        Timber.d("LOGING IN Got token ${response.success} ${response.requestToken}")
        response.success?.let {
            if (it) return response.requestToken
        }

        return null
    }

    private fun parseSessionResponse(response: SessionResponse) {
        Timber.d("LOGING IN Got session ${response.success} ${response.sessionID}")
        response.success?.let {
            if (it) {
                storage.putString(SESSION_ID, response.sessionID)
                sessionIdLD.value = response.sessionID
            }
        }
    }

    fun logout() {
        Timber.d("Logging out")
        val request = LogoutRequest(storage.getString(SESSION_ID)!!)
        val disposable = remote.logOut(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseLogoutResponse, ::handleFailure)

        addDisposable(disposable)
    }

    private fun parseLogoutResponse(response: DeleteSessionResponse) {
        Timber.d("Logged out ${response.success}")
        response.success?.let {
            if (it) {
                storage.putString(SESSION_ID, null)
                sessionIdLD.value = null
                logoutLD.value = true
            }
        }
    }

    fun getData() {
        Timber.d("Getting account details api_key: $API_KEY account_id: ${storage.getInt(ACCOUNT_ID)} session_id: ${storage.getString(SESSION_ID)}")
        val disposable = remote.getAccountDetails(storage.getString(SESSION_ID)!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseAccountResponse, ::handleFailure)

        val movieDisposable = remote.getFavoriteMovies(storage.getInt(ACCOUNT_ID), storage.getString(SESSION_ID)!!, null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseFavoriteMoviesResponse, ::handleFailure)

        val tvDisposable = remote.getFavoriteTVs(storage.getInt(ACCOUNT_ID), storage.getString(SESSION_ID)!!, null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(::onError)
            .subscribe(::parseFavoriteTVResponse, ::handleFailure)

        addDisposable(disposable)
        addDisposable(movieDisposable)
        addDisposable(tvDisposable)
    }

    private fun parseAccountResponse(response: AccountResponse) {
        Timber.d("Got account $response")
        storage.putInt(ACCOUNT_ID, response.id)
        accountLD.value = response
        increaseCounter()
    }

    private fun parseFavoriteMoviesResponse(response: MoviesResponse) {

        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
        }
        favMoviesLD.value = list
        increaseCounter()
    }

    private fun parseFavoriteTVResponse(response: TVResponse) {

        val list: MutableList<MovieItem> = mutableListOf()
        response.results.forEach {
            list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
        }
        favTVLD.value = list
        increaseCounter()
    }
}