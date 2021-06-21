package com.crabgore.moviesDB.data.search.services

import com.crabgore.moviesDB.data.search.models.SearchMovieResponse
import com.crabgore.moviesDB.data.search.models.SearchPeopleResponse
import com.crabgore.moviesDB.data.search.models.SearchTVResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Response<SearchMovieResponse>

    @GET("search/person")
    suspend fun searchPeople(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Response<SearchPeopleResponse>

    @GET("search/tv")
    suspend fun searchTV(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Response<SearchTVResponse>
}