package com.crabgore.moviesDB.domain

interface Storage {
    fun putString(key: String, value: String?)
    fun getString(key: String): String?
}