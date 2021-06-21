package com.crabgore.moviesDB.data.people.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PeopleDetailsResponse (
    val adult: Boolean? = null,
    val alsoKnownAs: List<String>? = null,
    val biography: String? = null,
    val birthday: String? = null,
    val deathday: String? = null,
    val gender: Int,
    val homepage: String? = null,
    val id: Long? = null,
    @field:Json(name="imdb_id")
    val imdbID: String? = null,
    @field:Json(name="known_for_department")
    val knownForDepartment: String? = null,
    val name: String? = null,
    @field:Json(name="place_of_birth")
    val placeOfBirth: String? = null,
    val popularity: Double? = null,
    @field:Json(name="profile_path")
    val profilePath: String? = null,
    @field:Json(name="movie_credits")
    val movieCredits: PeopleCreditsResponse? = null,
    @field:Json(name="tv_credits")
    val tvCredits: TVCastResponse? = null
)

@JsonClass(generateAdapter = true)
data class PeopleCreditsResponse(
    val cast: List<Cast>? = null,
    val crew: List<Cast>? = null,
    val id: Int? = null
)

@JsonClass(generateAdapter = true)
data class TVCastResponse(
    val cast: List<TVCast>? = null,
    val crew: List<TVCast>? = null,
    val id: Int? = null
)

@JsonClass(generateAdapter = true)
data class TVCast(
    @field:Json(name = "backdrop_path")
    val backdropPath: String? = null,
    val genreIDS: List<Long>? = null,
    val originalLanguage: String? = null,
    val firstAirDate: String? = null,
    val voteCount: Long? = null,
    @field:Json(name = "vote_average")
    val voteAverage: Double? = null,
    @field:Json(name = "poster_path")
    val posterPath: String? = null,
    val originalName: String? = null,
    val originCountry: List<String>? = null,
    val id: Int,
    val overview: String? = null,
    val name: String? = null,
    val popularity: Double? = null,
    val character: String? = null,
    val creditID: String? = null,
    val episodeCount: Long? = null,
    val job: String? = null
)

@JsonClass(generateAdapter = true)
data class Cast(
    @field:Json(name = "poster_path")
    val posterPath: String? = null,
    val video: Boolean? = null,
    val id: Int,
    val overview: String? = null,
    @field:Json(name = "release_date")
    val releaseDate: String? = null,
    val title: String? = null,
    val adult: Boolean? = null,
    @field:Json(name = "backdrop_path")
    val backdropPath: String? = null,
    val voteCount: Long? = null,
    val genreIDS: List<Long>? = null,
    @field:Json(name = "vote_average")
    val voteAverage: Double? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val popularity: Double? = null,
    val character: String? = null,
    val creditID: String? = null,
    val order: Long? = null,
    val department: String? = null,
    val job: String? = null
)