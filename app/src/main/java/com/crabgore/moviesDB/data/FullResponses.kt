package com.crabgore.moviesDB.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsResponse (
    val adult: Boolean,
    @field:Json(name="backdrop_path")
    val backdropPath: String?,
    val belongsToCollection: BelongsToCollection?,
    val budget: Long?,
    val genres: List<Genre>?,
    val homepage: String?,
    val id: Long,
    @field:Json(name="imdb_id")
    val imdbID: String?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @field:Json(name="poster_path")
    val posterPath: String?,
    val productionCompanies: List<ProductionCompany>?,
    @field:Json(name="production_countries")
    val productionCountries: List<ProductionCountry>?,
    @field:Json(name="release_date")
    val releaseDate: String?,
    val revenue: Long?,
    val runtime: Long?,
    val spokenLanguages: List<SpokenLanguage>?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    @field:Json(name="vote_average")
    val voteAverage: Double?,
    val voteCount: Long?,
    val credits: MovieCreditsResponse? = null
)

@JsonClass(generateAdapter = true)
data class TVDetailsResponse (
    @field:Json(name="backdrop_path")
    val backdropPath: String?,
    val createdBy: List<CreatedBy>?,
    @field:Json(name="episode_run_time")
    val episodeRunTime: List<Int>?,
    @field:Json(name="first_air_date")
    val firstAirDate: String?,
    val genres: List<Genre>?,
    val homepage: String?,
    val id: Long,
    val inProduction: Boolean?,
    val languages: List<String>?,
    val lastAirDate: String?,
    val lastEpisodeToAir: LastEpisodeToAir?,
    val name: String?,
    val nextEpisodeToAir: Any? = null,
    val networks: List<Network>?,
    @field:Json(name="number_of_episodes")
    val numberOfEpisodes: Long?,
    @field:Json(name="number_of_seasons")
    val numberOfSeasons: Long?,
    val originCountry: List<String>?,
    val originalLanguage: String?,
    val originalName: String?,
    val overview: String?,
    val popularity: Double?,
    @field:Json(name="poster_path")
    val posterPath: String?,
    val productionCompanies: List<Network>?,
    @field:Json(name="production_countries")
    val productionCountries: List<ProductionCountry>?,
    val seasons: List<Season>?,
    val spokenLanguages: List<SpokenLanguage>?,
    val status: String?,
    val tagline: String?,
    val type: String?,
    @field:Json(name="vote_average")
    val voteAverage: Double?,
    val voteCount: Long?,
    val credits: MovieCreditsResponse? = null
)

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
data class Genre (
    val id: Long?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class ProductionCompany (
    val id: Long,
    val logoPath: String? = null,
    val name: String,
    val originCountry: String
)

@JsonClass(generateAdapter = true)
data class ProductionCountry (
    val iso3166_1: String?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class CreatedBy (
    val id: Long?,
    val creditID: String?,
    val name: String?,
    val gender: Long?,
    val profilePath: String?
)

@JsonClass(generateAdapter = true)
data class LastEpisodeToAir (
    val airDate: String?,
    val episodeNumber: Long?,
    val id: Long,
    val name: String?,
    val overview: String?,
    val productionCode: String?,
    val seasonNumber: Long?,
    val stillPath: String?,
    val voteAverage: Long?,
    val voteCount: Long?
)

@JsonClass(generateAdapter = true)
data class Network (
    val name: String?,
    val id: Long?,
    val logoPath: String? = null,
    val originCountry: String?
)

@JsonClass(generateAdapter = true)
data class Season (
    val airDate: String? = null,
    val episodeCount: Long?,
    val id: Long?,
    val name: String?,
    val overview: String?,
    val posterPath: String? = null,
    val seasonNumber: Long?
)

@JsonClass(generateAdapter = true)
data class SpokenLanguage (
    val englishName: String?,
    val iso639_1: String?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class BelongsToCollection (
    val id: Long,
    val name: String,
    val posterPath: String,
    val backdropPath: String
)

@JsonClass(generateAdapter = true)
data class AccountResponse (
    val avatar: Avatar? = null,
    val id: Int,
    val iso639_1: String? = null,
    val iso3166_1: String? = null,
    val name: String? = null,
    val includeAdult: Boolean? = null,
    val username: String? = null
)

@JsonClass(generateAdapter = true)
data class Avatar (
    val gravatar: Gravatar? = null,
    val tmdb: Tmdb? = null
)

@JsonClass(generateAdapter = true)
data class Gravatar (
    val hash: String? = null
)

@JsonClass(generateAdapter = true)
data class Tmdb (
    val avatarPath: String? = null
)

