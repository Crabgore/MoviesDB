package com.crabgore.moviesDB.domain.repositories

import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.data.*
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.repositories.interfaces.UserRepository
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseRepository(), UserRepository {

    override suspend fun login(username: String, password: String): String? {
        val tokenResponse = remote.getToken()
        val token = parseTokenResponse(tokenResponse)

        val authRequest = AuthWithLoginRequest(username, password, token!!)
        val authedTokenResponse = remote.authWithLogin(authRequest)
        val authedToken = parseTokenResponse(authedTokenResponse)

        val sessionRequest = RequestToken(authedToken!!)
        val sessionIDResponse = remote.sessionId(sessionRequest)
        return parseSessionResponse(sessionIDResponse)
    }

    override suspend fun logout(): Boolean {
        val request = LogoutRequest(storage.getString(SESSION_ID)!!)
        val response = remote.logOut(request)
        return parseLogoutResponse(response)
    }

    override suspend fun getAccountDetails(): Resource<AccountResponse> {
        val account = remote.getAccountDetails(storage.getString(SESSION_ID)!!)
        return parseAccountResponse(account)
    }

    override suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>> {
        val movies = remote.getFavoriteMovies(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            page
        )
        return parseFavoriteMoviesResponse(movies)
    }

    override suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>> {
        val tvs = remote.getFavoriteTVs(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            page
        )
        return parseFavoriteTVResponse(tvs)
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

    private fun parseSessionResponse(response: Response<SessionResponse>): String? {
        return if (response.isSuccessful) {
            Timber.d("LOGING IN PARSING SESSION RESPONSE ${response.body()}")
            var result: String? = null
            response.body()?.let {
                Timber.d("LOGING IN Got session ${it.success} ${it.sessionID}")
                it.success?.let { success ->
                    if (success) {
                        storage.putString(SESSION_ID, it.sessionID)
                        result = it.sessionID
                    }
                }
            }
            result
        } else {
            Timber.d("PARSING SESSION RESPONSE ERROR ${response.message()}")
            null
        }
    }

    private fun parseLogoutResponse(response: Response<DeleteSessionResponse>): Boolean {
        return if (response.isSuccessful) {
            Timber.d("LOGING OUT ${response.body()}")
            var result = false
            response.body()?.let {
                Timber.d("Logged out ${it.success}")
                it.success?.let { success ->
                    if (success) {
                        storage.putString(SESSION_ID, null)
                        result = true
                    }
                }
            }
            result
        } else {
            Timber.d("LOGING OUT ERROR ${response.message()}")
            false
        }
    }

    private fun parseAccountResponse(response: Response<AccountResponse>): Resource<AccountResponse> {
        return if (response.isSuccessful) {
            Timber.d("Got account ${response.body()}")
            var result: Resource<AccountResponse> = Resource.loading(null)
            response.body()?.let {
                storage.putInt(ACCOUNT_ID, it.id)
                result = Resource.success(it)
            }
            result
        } else Resource.error(data = null, message = response.message())
    }

    private fun parseFavoriteMoviesResponse(response: Response<MoviesResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got favorite movies ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.let {
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
            }
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }

    private fun parseFavoriteTVResponse(response: Response<TVResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got favorite tvs $response")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.let {
                it.results.forEach { tv ->
                    list.add(MovieItem(tv.id, tv.name, tv.posterPath, tv.voteAverage, false))
                }
            }
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }
}