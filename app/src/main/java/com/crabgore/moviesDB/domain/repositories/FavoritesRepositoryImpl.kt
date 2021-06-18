package com.crabgore.moviesDB.domain.repositories

import com.crabgore.moviesDB.Const.MediaTypes.Companion.MOVIE
import com.crabgore.moviesDB.Const.MediaTypes.Companion.TV
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.data.MarkAsFavoriteRequest
import com.crabgore.moviesDB.data.MarkAsFavoriteResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.repositories.interfaces.FavoritesRepository
import com.crabgore.moviesDB.domain.storage.Storage
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseRepository(), FavoritesRepository {

    override suspend fun markMovieAsFavorite(movieId: Int, isInFavorite: Boolean): Boolean {
        val request = MarkAsFavoriteRequest(MOVIE, movieId, isInFavorite)

        val response = remote.markAsFavorite(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            request
        )
        return parseMarkAsFavoriteResponse(response)
    }

    override suspend fun markTVAsFavorite(tvId: Int, isInFavorite: Boolean): Boolean {
        val request = MarkAsFavoriteRequest(TV, tvId, isInFavorite)

        val response = remote.markAsFavorite(
            storage.getInt(ACCOUNT_ID),
            storage.getString(SESSION_ID)!!,
            request
        )
        return parseMarkAsFavoriteResponse(response)
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
}
