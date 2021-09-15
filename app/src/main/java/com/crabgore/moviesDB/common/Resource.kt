package com.crabgore.moviesDB.common

sealed class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    class Success<T>(data: T): Resource<T>(status = Status.SUCCESS, data = data, message = null)
    class Error<T>(data: T?, message: String): Resource<T>(status = Status.ERROR, data = data, message = message)
    class Loading<T>(data: T?): Resource<T>(status = Status.LOADING, data = data, message = null)
}