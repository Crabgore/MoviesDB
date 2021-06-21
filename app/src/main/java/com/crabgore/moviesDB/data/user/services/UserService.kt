package com.crabgore.moviesDB.data.user.services

import com.crabgore.moviesDB.data.user.requests.AuthWithLoginRequest
import com.crabgore.moviesDB.data.user.requests.LogoutRequest
import com.crabgore.moviesDB.data.user.requests.RequestToken
import com.crabgore.moviesDB.data.user.models.DeleteSessionResponse
import com.crabgore.moviesDB.data.user.models.SessionResponse
import com.crabgore.moviesDB.data.user.models.TokenResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("authentication/token/new")
    suspend fun requestToken(
        @Query("api_key") apiKey: String
    ): Response<TokenResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun authWithLogin(
        @Query("api_key") apiKey: String,
        @Body request: AuthWithLoginRequest
    ): Response<TokenResponse>

    @POST("authentication/session/new")
    suspend fun sessionId(
        @Query("api_key") apiKey: String,
        @Body request: RequestToken
    ): Response<SessionResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun logOut(
        @Query("api_key") apiKey: String,
        @Body request: LogoutRequest
    ): Response<DeleteSessionResponse>
}