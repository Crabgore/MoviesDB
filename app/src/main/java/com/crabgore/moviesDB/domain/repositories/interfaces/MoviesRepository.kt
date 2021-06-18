package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.data.MovieDetailsResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.crabgore.moviesDB.ui.items.MovieItem

interface MoviesRepository {

    var maxPages: Int
    var castListStore: Resource<List<CreditsItem>>
    var crewListStore: Resource<List<CreditsItem>>
    suspend fun getNowPlayingMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getPopularMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getTopRatedMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getUpcomingMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getMovieDetails(id: Int): Resource<MovieDetailsResponse>
    suspend fun getMovieAccountState(id: Int): Boolean
}