package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.people.models.PeopleDetailsResponse
import com.crabgore.moviesDB.ui.items.CreditsItem

interface PeopleDetailsRepository {

    var movieCastListStore: Resource<List<CreditsItem>>
    var movieCrewListStore: Resource<List<CreditsItem>>
    var tvCastListStore: Resource<List<CreditsItem>>
    var tvCrewListStore: Resource<List<CreditsItem>>
    suspend fun getPeopleDetails(id: Int): Resource<PeopleDetailsResponse>
}