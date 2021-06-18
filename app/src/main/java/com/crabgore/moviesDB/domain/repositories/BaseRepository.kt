package com.crabgore.moviesDB.domain.repositories

import retrofit2.Response

open class BaseRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Response<T> {

        return call.invoke()
    }
}