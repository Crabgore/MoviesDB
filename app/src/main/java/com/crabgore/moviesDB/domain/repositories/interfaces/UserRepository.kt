package com.crabgore.moviesDB.domain.repositories.interfaces

interface UserRepository {

    suspend fun login(username: String, password: String): String?
    suspend fun logout(): Boolean
}