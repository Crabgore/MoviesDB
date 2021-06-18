package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.ui.items.SearchItem

interface SearchRepository {

    suspend fun getSearchMovieResults(query: String): Resource<List<SearchItem>>
    suspend fun getSearchTVResults(query: String): Resource<List<SearchItem>>
    suspend fun getSearchPeopleResults(query: String): Resource<List<SearchItem>>
}