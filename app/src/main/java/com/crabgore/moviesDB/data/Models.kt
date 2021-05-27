package com.crabgore.moviesDB.data

data class MovieInfo (
    val adult: Boolean,
    val backdropPath: String,
    val genreIDS: List<String>,
    val id: Long,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double
)

data class TVInfo(
    val backdropPath: String,
    val firstAirDate: String,
    val genreIDS: List<String>,
    val id: Long,
    val name: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val posterPath: String,
    val voteAverage: Double
)