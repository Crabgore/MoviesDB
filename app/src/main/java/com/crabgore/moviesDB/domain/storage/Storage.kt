package com.crabgore.moviesDB.domain.storage

interface Storage {
    fun putString(key: String, value: String?)
    fun getString(key: String): String?

    fun putInt(key: String, value: Int)
    fun getInt(key: String): Int
}