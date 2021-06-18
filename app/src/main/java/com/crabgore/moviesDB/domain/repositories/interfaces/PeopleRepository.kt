package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.data.PeopleDetailsResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.crabgore.moviesDB.ui.items.PeopleItem

interface PeopleRepository {

    var maxPages: Int
    var movieCastListStore: Resource<List<CreditsItem>>
    var movieCrewListStore: Resource<List<CreditsItem>>
    var tvCastListStore: Resource<List<CreditsItem>>
    var tvCrewListStore: Resource<List<CreditsItem>>
    suspend fun getPopularPeople(page: Int?): Resource<List<PeopleItem>>
    suspend fun getPeopleDetails(id: Int): Resource<PeopleDetailsResponse>
}