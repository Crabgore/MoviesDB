package com.crabgore.moviesDB.data.user.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.data.user.models.DeleteSessionResponse
import com.crabgore.moviesDB.data.user.models.SessionResponse
import com.crabgore.moviesDB.data.user.models.TokenResponse
import com.crabgore.moviesDB.data.user.requests.AuthWithLoginRequest
import com.crabgore.moviesDB.data.user.requests.LogoutRequest
import com.crabgore.moviesDB.data.user.requests.RequestToken
import com.crabgore.moviesDB.data.user.services.UserService
import com.crabgore.moviesDB.domain.storage.Storage
import retrofit2.Response
import timber.log.Timber

class UserRepository(
    private val userService: UserService,
    private val storage: Storage
) {
    suspend fun login(username: String, password: String): String? {
        val tokenResponse = userService.requestToken(API_KEY)
        val token = parseTokenResponse(tokenResponse)

        val authRequest = AuthWithLoginRequest(username, password, token!!)
        val authedTokenResponse = userService.authWithLogin(API_KEY, authRequest)
        val authedToken = parseTokenResponse(authedTokenResponse)

        val sessionRequest = RequestToken(authedToken!!)
        val sessionIDResponse = userService.sessionId(API_KEY, sessionRequest)
        return parseSessionResponse(sessionIDResponse)
    }

    suspend fun logout(): Boolean {
        val request = LogoutRequest(storage.getString(SESSION_ID)!!)
        val response = userService.logOut(API_KEY, request)
        return parseLogoutResponse(response)
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
}