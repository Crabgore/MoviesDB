package com.crabgore.moviesDB.data.movies.models

import com.crabgore.moviesDB.common.Genre
import com.crabgore.moviesDB.common.MovieCreditsResponse
import com.crabgore.moviesDB.common.ProductionCountry
import com.crabgore.moviesDB.common.SpokenLanguage
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
data class ProductionCompany (
    val id: Long,
    val logoPath: String? = null,
    val name: String,
    val originCountry: String
)

@JsonClass(generateAdapter = true)
data class BelongsToCollection (
    val id: Long,
    val name: String,
    val posterPath: String,
    val backdropPath: String
)