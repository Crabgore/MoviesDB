package com.crabgore.moviesDB.domain.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Storage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("Koin", MODE_PRIVATE)

    fun putString(key: String, value: String?) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putInt(key: String, value: Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }
}