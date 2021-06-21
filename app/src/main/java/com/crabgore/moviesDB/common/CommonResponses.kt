package com.crabgore.moviesDB.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Dates(
    val maximum: String,
    val minimum: String
)

@JsonClass(generateAdapter = true)
data class Genre (
    val id: Long?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class ProductionCountry (
    val iso3166_1: String?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class SpokenLanguage (
    val englishName: String?,
    val iso639_1: String?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class MovieCreditsResponse(
    val id: Int? = null,
    val cast: List<MovieCast>? = null,
    val crew: List<MovieCast>? = null
)

@JsonClass(generateAdapter = true)
data class MovieCast(
    val adult: Boolean? = null,
    val gender: Long? = null,
    val id: Int,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val originalName: String? = null,
    val popularity: Double? = null,
    @field:Json(name = "profile_path")
    val profilePath: String? = null,
    val castID: Long? = null,
    val character: String? = null,
    @field:Json(name = "credit_id")
    val creditID: String? = null,
    val order: Long? = null,
    val department: String? = null,
    val job: String? = null
)

@JsonClass(generateAdapter = true)
data class AccountStateResponse (
    val id: Long? = null,
    val favorite: Boolean? = null,
    val rated: Boolean? = null,
    val watchlist: Boolean? = null
)

