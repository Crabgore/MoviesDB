package com.crabgore.moviesDB.data.tv.services

import com.crabgore.moviesDB.data.tv.models.TVResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TVService {

    @GET("tv/on_the_air")
    suspend fun onTheAirTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>

    @GET("tv/popular")
    suspend fun popularTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>

    @GET("tv/top_rated")
    suspend fun topRatedTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>
}