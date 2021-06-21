package com.crabgore.moviesDB.data.tv.models

import com.crabgore.moviesDB.common.Dates
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TVResponse(
    val dates: Dates?,
    val page: Long,
    val results: List<TV>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    val totalResults: Int?
)

@JsonClass(generateAdapter = true)
data class TV(
    @field:Json(name = "backdrop_path")
    val backdropPath: String?,
    @field:Json(name = "first_air_date")
    val firstAirDate: String?,
    val genreIDS: List<Long>?,
    val id: Int,
    val name: String,
    val originCountry: List<String>?,
    val originalLanguage: String?,
    val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    @field:Json(name = "poster_path")
    val posterPath: String?,
    @field:Json(name = "vote_average")
    val voteAverage: Double?,
    val voteCount: Long?
)