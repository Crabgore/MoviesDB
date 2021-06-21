package com.crabgore.moviesDB.data.favorites.services

import com.crabgore.moviesDB.common.AccountStateResponse
import com.crabgore.moviesDB.data.favorites.models.MarkAsFavoriteResponse
import com.crabgore.moviesDB.data.favorites.requests.MarkAsFavoriteRequest
import com.crabgore.moviesDB.data.movies.models.MoviesResponse
import com.crabgore.moviesDB.data.tv.models.TVResponse
import retrofit2.Response
import retrofit2.http.*

interface FavoritesService {

    @GET("movie/{movie_id}/account_states")
    suspend fun movieAccountState(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<AccountStateResponse>

    @GET("tv/{tv_id}/account_states")
    suspend fun tvAccountState(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<AccountStateResponse>

    @POST("account/{account_id}/favorite")
    suspend fun markAsFavorite(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Body request: MarkAsFavoriteRequest
    ): Response<MarkAsFavoriteResponse>

    @GET("account/{account_id}/favorite/movies")
    suspend fun favoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<MoviesResponse>

    @GET("account/{account_id}/favorite/tv")
    suspend fun favoriteTVs(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>
}