package com.crabgore.moviesDB.data.movies.models

import com.crabgore.moviesDB.common.Dates
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesResponse(
    val dates: Dates?,
    val page: Long?,
    val results: List<Movie>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    val totalResults: Int?
)

@JsonClass(generateAdapter = true)
data class Movie(
    val adult: Boolean,
    @field:Json(name = "backdrop_path")
    val backdropPath: String?,
    val genreIDS: List<Long>?,
    val id: Int,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @field:Json(name = "poster_path")
    val posterPath: String?,
    @field:Json(name = "release_date")
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @field:Json(name = "vote_average")
    val voteAverage: Double?,
    val voteCount: Long?
)