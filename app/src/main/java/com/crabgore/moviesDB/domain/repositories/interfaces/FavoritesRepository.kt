package com.crabgore.moviesDB.domain.repositories.interfaces

interface FavoritesRepository {
    suspend fun markMovieAsFavorite(movieId: Int, isInFavorite: Boolean): Boolean
    suspend fun markTVAsFavorite(tvId: Int, isInFavorite: Boolean): Boolean
}