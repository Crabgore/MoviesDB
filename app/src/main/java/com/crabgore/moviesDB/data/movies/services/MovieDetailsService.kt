package com.crabgore.moviesDB.data.movies.services

import com.crabgore.moviesDB.data.movies.models.MovieDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailsService {

    @GET("movie/{movie_id}")
    suspend fun movieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?
    ): Response<MovieDetailsResponse>
}