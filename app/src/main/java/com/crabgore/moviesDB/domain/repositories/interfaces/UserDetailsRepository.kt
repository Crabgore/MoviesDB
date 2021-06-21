package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.user.models.AccountResponse
import com.crabgore.moviesDB.ui.items.MovieItem

interface UserDetailsRepository {

    suspend fun getAccountDetails(): Resource<AccountResponse>
    suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>>
}