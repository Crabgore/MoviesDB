package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.tv.models.TVDetailsResponse
import com.crabgore.moviesDB.ui.items.CreditsItem

interface TVDetailsRepository {

    var castListStore: Resource<List<CreditsItem>>
    var crewListStore: Resource<List<CreditsItem>>
    suspend fun getTVDetails(id: Int): Resource<TVDetailsResponse>
}