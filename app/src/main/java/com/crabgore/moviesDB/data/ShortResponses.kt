package com.crabgore.moviesDB.data

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
data class TVResponse(
    val dates: Dates?,
    val page: Long,
    val results: List<TV>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    val totalResults: Int?
)

@JsonClass(generateAdapter = true)
data class PeopleResponse(
    val page: Long,
    val results: List<People>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    val totalResults: Long?
)

@JsonClass(generateAdapter = true)
data class SearchMovieResponse(
    val page: Long? = null,
    val results: List<Movie>? = null,
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

@JsonClass(generateAdapter = true)
data class SearchPeopleResponse(
    val page: Long? = null,
    val results: List<People>? = null,
    val totalPages: Long? = null,
    val totalResults: Long? = null
)

@JsonClass(generateAdapter = true)
data class Dates(
    val maximum: String,
    val minimum: String
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

@JsonClass(generateAdapter = true)
data class PeopleCreditsResponse(
    val cast: List<Cast>? = null,
    val crew: List<Cast>? = null,
    val id: Int
)

@JsonClass(generateAdapter = true)
data class TVCastResponse(
    val cast: List<TVCast>? = null,
    val crew: List<TVCast>? = null,
    val id: Int
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

@JsonClass(generateAdapter = true)
data class MovieCreditsResponse(
    val id: Int,
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
data class AuthResponse (
    val success: Boolean? = null,
    val statusCode: Int? = null,
    val statusMessage: String? = null
)

@JsonClass(generateAdapter = true)
data class TokenResponse (
    val success: Boolean? = null,
    val expiresAt: String? = null,
    @field:Json(name = "request_token")
    val requestToken: String? = null
)

@JsonClass(generateAdapter = true)
data class SessionResponse (
    val success: Boolean? = null,
    @field:Json(name = "session_id")
    val sessionID: String? = null
)

@JsonClass(generateAdapter = true)
data class DeleteSessionResponse (
    val success: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class MarkAsFavoriteResponse (
    val success: Boolean? = null,
    @field:Json(name = "status_code")
    val statusCode: Int? = null,
    @field:Json(name = "status_message")
    val statusMessage: String? = null
)

@JsonClass(generateAdapter = true)
data class AccountStateResponse (
    val id: Long? = null,
    val favorite: Boolean? = null,
    val rated: Boolean? = null,
    val watchlist: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class Rated (
    val value: Long? = null
)

