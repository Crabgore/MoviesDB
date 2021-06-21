package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.ui.items.PeopleItem

interface PeopleRepository {

    var maxPages: Int
    suspend fun getPopularPeople(page: Int?): Resource<List<PeopleItem>>
}