package com.crabgore.moviesDB.data.people.services

import com.crabgore.moviesDB.data.people.models.PeopleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleService {

    @GET("person/popular")
    suspend fun popularPeople(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<PeopleResponse>
}