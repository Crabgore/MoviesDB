package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.ui.items.MovieItem

interface MoviesRepository {

    var maxPages: Int
    suspend fun getNowPlayingMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getPopularMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getTopRatedMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getUpcomingMovies(page: Int?): Resource<List<MovieItem>>
}