package com.crabgore.moviesDB.data.tv.services

import com.crabgore.moviesDB.data.tv.models.TVDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVDetailsServices {

    @GET("tv/{tv_id}")
    suspend fun tvDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?
    ): Response<TVDetailsResponse>
}