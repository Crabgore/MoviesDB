package com.crabgore.moviesDB.data.tv.models

import com.crabgore.moviesDB.common.Genre
import com.crabgore.moviesDB.common.MovieCreditsResponse
import com.crabgore.moviesDB.common.ProductionCountry
import com.crabgore.moviesDB.common.SpokenLanguage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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