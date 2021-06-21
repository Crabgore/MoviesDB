package com.crabgore.moviesDB.data.search.models

import com.crabgore.moviesDB.data.movies.models.Movie
import com.crabgore.moviesDB.data.people.models.People
import com.crabgore.moviesDB.data.tv.models.TV
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchMovieResponse(
    val page: Long? = null,
    val results: List<Movie>? = null,
    val totalPages: Long? = null,
    val totalResults: Long? = null
)

@JsonClass(generateAdapter = true)
data class SearchPeopleResponse(
    val page: Long? = null,
    val results: List<People>? = null,
    val totalPages: Long? = null,
    val totalResults: Long? = null
)

@JsonClass(generateAdapter = true)
data class SearchTVResponse(
    val page: Long? = null,
    val results: List<TV>? = null,
    val totalPages: Long? = null,
    val totalResults: Long? = null
)