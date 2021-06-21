package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.ui.items.MovieItem

interface FavoritesRepository {
    var maxPages: Int
    suspend fun getMovieAccountState(id: Int): Boolean
    suspend fun getTVsAccountState(id: Int): Boolean
    suspend fun markMovieAsFavorite(movieId: Int, isInFavorite: Boolean): Boolean
    suspend fun markTVAsFavorite(tvId: Int, isInFavorite: Boolean): Boolean
    suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>>
}