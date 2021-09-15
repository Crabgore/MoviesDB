package com.crabgore.moviesDB.data.favorites.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.Const.MediaTypes.Companion.MOVIE
import com.crabgore.moviesDB.Const.MediaTypes.Companion.TV
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.AccountStateResponse
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.data.favorites.models.MarkAsFavoriteResponse
import com.crabgore.moviesDB.data.favorites.requests.MarkAsFavoriteRequest
import com.crabgore.moviesDB.data.favorites.services.FavoritesService
import com.crabgore.moviesDB.data.movies.models.MoviesResponse
import com.crabgore.moviesDB.data.tv.models.TVResponse
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber

class FavoritesRepository(
    private val api: FavoritesService,
    private val storage: Storage
) {
    var maxPages = Int.MAX_VALUE

    suspend fun getMovieAccountState(id: Int): Boolean {
        val response =
            api.movieAccountState(id, API_KEY, storage.getString(SESSION_ID)!!)
        return parseAccountState(response)
    }

    suspend fun getTVsAccountState(id: Int): Boolean {
        val response = api.tvAccountState(id, API_KEY, storage.getString(SESSION_ID)!!)
        return parseAccountState(response)
    }

    suspend fun markMovieAsFavorite(movieId: Int, isInFavorite: Boolean): Boolean {
        val request = MarkAsFavoriteRequest(MOVIE, movieId, isInFavorite)

        val response = api.markAsFavorite(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            API_KEY,
            request
        )
        return parseMarkAsFavoriteResponse(response)
    }

    suspend fun markTVAsFavorite(tvId: Int, isInFavorite: Boolean): Boolean {
        val request = MarkAsFavoriteRequest(TV, tvId, isInFavorite)

        val response = api.markAsFavorite(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            API_KEY,
            request
        )
        return parseMarkAsFavoriteResponse(response)
    }

    suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>> {
        val response = api.favoriteMovies(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            API_KEY,
            getLanguage(),
            page
        )
        return parseMovieResponse(response)
    }

    suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>> {
        val response = api.favoriteTVs(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            API_KEY, getLanguage(),
            page
        )

        return parseTVResponse(response)
    }

    private fun parseAccountState(response: Response<AccountStateResponse>): Boolean {
        return if (response.isSuccessful) {
            Timber.d("Got Movie AccountState ${response.body()}")
            var result = false
            response.body()?.favorite?.let {
                result = it
            }
            result
        } else {
            Timber.d("AccountState error ${response.message()}")
            false
        }
    }

    private fun parseMarkAsFavoriteResponse(response: Response<MarkAsFavoriteResponse>): Boolean {
        return if (response.isSuccessful) {
            Timber.d("Got MarkAsFavoriteResponse ${response.body()}")
            var result = false
            response.body()?.success?.let {
                result = true
            }
            result
        } else {
            Timber.d("MarkAsFavoriteResponse error ${response.message()}")
            false
        }
    }

    private fun parseMovieResponse(response: Response<MoviesResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got Movie response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
            }
            maxPages = response.body()?.totalPages!!
            Resource.Success(list)
        } else Resource.Error(data = null, message = response.message())
    }

    private fun parseTVResponse(response: Response<TVResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got TV response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
            }
            maxPages = response.body()?.totalPages!!
            Resource.Success(list)
        } else Resource.Error(data = null, message = response.message())
    }
}
