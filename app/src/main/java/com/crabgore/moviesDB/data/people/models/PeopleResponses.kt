package com.crabgore.moviesDB.data.people.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PeopleResponse(
    val page: Long,
    val results: List<People>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    val totalResults: Long?
)

@JsonClass(generateAdapter = true)
data class People(
    val adult: Boolean?,
    val gender: Long?,
    val id: Int,
    val knownFor: List<KnownFor>?,
    val knownForDepartment: String?,
    val name: String?,
    val popularity: Double?,
    @field:Json(name = "profile_path")
    val profilePath: String? = null
)

@JsonClass(generateAdapter = true)
data class KnownFor(
    val backdropPath: String? = null,
    val firstAirDate: String? = null,
    val genreIDS: List<Long>?,
    val id: Long,
    val mediaType: String?,
    val name: String? = null,
    val originCountry: List<String>? = null,
    val originalLanguage: String?,
    val originalName: String? = null,
    val overview: String?,
    val posterPath: String?,
    val voteAverage: Double?,
    val voteCount: Long?,
    val adult: Boolean? = null,
    val originalTitle: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null
)