package com.crabgore.moviesDB.data.user.services

import com.crabgore.moviesDB.data.user.models.AccountResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserDetailsService {

    @GET("account")
    suspend fun accountDetails(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<AccountResponse>
}