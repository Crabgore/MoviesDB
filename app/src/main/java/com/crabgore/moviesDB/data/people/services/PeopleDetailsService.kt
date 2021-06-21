package com.crabgore.moviesDB.data.people.services

import com.crabgore.moviesDB.data.people.models.PeopleDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleDetailsService {

    @GET("person/{person_id}")
    suspend fun peopleDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?
    ): Response<PeopleDetailsResponse>
}