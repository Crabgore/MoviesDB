package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.data.AccountResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.ui.items.MovieItem

interface UserRepository {

    suspend fun login(username: String, password: String): String?
    suspend fun logout(): Boolean
    suspend fun getAccountDetails(): Resource<AccountResponse>
    suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>>
    suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>>
}