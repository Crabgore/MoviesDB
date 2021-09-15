package com.crabgore.moviesDB.data.user.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.data.favorites.services.FavoritesService
import com.crabgore.moviesDB.data.movies.models.MoviesResponse
import com.crabgore.moviesDB.data.tv.models.TVResponse
import com.crabgore.moviesDB.data.user.models.AccountResponse
import com.crabgore.moviesDB.data.user.services.UserDetailsService
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber

class UserDetailsRepository(
    private val userDetailsService: UserDetailsService,
    private val favoritesService: FavoritesService,
    private val storage: Storage
) {
    suspend fun getAccountDetails(): Resource<AccountResponse> {
        val account = userDetailsService.accountDetails(API_KEY, storage.getString(SESSION_ID)!!)
        return parseAccountResponse(account)
    }

    suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>> {
        val movies = favoritesService.favoriteMovies(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            API_KEY,
            getLanguage(),
            page
        )
        return parseFavoriteMoviesResponse(movies)
    }

    suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>> {
        val tvs = favoritesService.favoriteTVs(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            API_KEY,
            getLanguage(),
            page
        )
        return parseFavoriteTVResponse(tvs)
    }

    private fun parseAccountResponse(response: Response<AccountResponse>): Resource<AccountResponse> {
        return if (response.isSuccessful) {
            Timber.d("Got account ${response.body()}")
            var result: Resource<AccountResponse> = Resource.Loading(null)
            response.body()?.let {
                storage.putInt(ACCOUNT_ID, it.id)
                result = Resource.Success(it)
            }
            result
        } else Resource.Error(data = null, message = response.message())
    }

    private fun parseFavoriteMoviesResponse(response: Response<MoviesResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got favorite movies ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.let {
                it.results.forEach { movie ->
                    list.add(
                        MovieItem(
                            movie.id,
                            movie.title,
                            movie.posterPath,
                            movie.voteAverage,
                            movie.adult
                        )
                    )
                }
            }
            Resource.Success(list)
        } else Resource.Error(data = null, message = response.message())
    }

    private fun parseFavoriteTVResponse(response: Response<TVResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got favorite tvs $response")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.let {
                it.results.forEach { tv ->
                    list.add(MovieItem(tv.id, tv.name, tv.posterPath, tv.voteAverage, false))
                }
            }
            Resource.Success(list)
        } else Resource.Error(data = null, message = response.message())
    }
}